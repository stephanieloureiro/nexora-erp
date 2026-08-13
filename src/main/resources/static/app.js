const state = {
    token: localStorage.getItem("nexoraToken"),
};

const money = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
});

const loginPanel = document.querySelector("#loginPanel");
const appPanel = document.querySelector("#appPanel");
const loginMessage = document.querySelector("#loginMessage");
const globalMessage = document.querySelector("#globalMessage");

document.querySelector("#loginForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = event.currentTarget;

    try {
        const data = await request("/api/auth/login", {
            method: "POST",
            body: formDataJson(form),
            auth: false,
        });

        state.token = data.accessToken;
        localStorage.setItem("nexoraToken", state.token);
        loginMessage.textContent = "";
        showApp();
        await loadAll();
    } catch (error) {
        loginMessage.textContent = error.message;
    }
});

document.querySelector("#logoutButton").addEventListener("click", () => {
    localStorage.removeItem("nexoraToken");
    state.token = null;
    appPanel.classList.add("hidden");
    loginPanel.classList.remove("hidden");
});

document.querySelector("#refreshButton").addEventListener("click", loadAll);

document.querySelectorAll("[data-tab]").forEach((button) => {
    button.addEventListener("click", () => {
        document.querySelectorAll("[data-tab]").forEach((item) => item.classList.remove("active"));
        document.querySelectorAll(".tab-view").forEach((item) => item.classList.remove("active"));
        button.classList.add("active");
        document.querySelector(`#${button.dataset.tab}`).classList.add("active");
    });
});

document.querySelector("#customerForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    await submitForm(event.currentTarget, "/api/customers", "Cliente cadastrado.");
});

document.querySelector("#productForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    await submitForm(event.currentTarget, "/api/products", "Produto cadastrado.");
});

document.querySelector("#stockForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    await submitForm(event.currentTarget, "/api/stock-movements", "Movimento registrado.");
});

document.querySelector("#orderForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    const payload = {
        customerId: numberValue(form.customerId.value),
        items: [{
            productId: numberValue(form.productId.value),
            quantity: numberValue(form.quantity.value),
        }],
    };

    try {
        await request("/api/sales-orders", { method: "POST", body: payload });
        form.reset();
        setMessage("Pedido criado.");
        await loadAll();
    } catch (error) {
        setMessage(error.message, true);
    }
});

if (state.token) {
    showApp();
    loadAll();
}

function showApp() {
    loginPanel.classList.add("hidden");
    appPanel.classList.remove("hidden");
}

async function submitForm(form, url, successMessage) {
    try {
        await request(url, { method: "POST", body: formDataJson(form) });
        form.reset();
        setMessage(successMessage);
        await loadAll();
    } catch (error) {
        setMessage(error.message, true);
    }
}

async function loadAll() {
    if (!state.token) {
        return;
    }

    try {
        const [customers, products, stock, orders, sales, stockSummary, topProducts, audit] = await Promise.all([
            page("/api/customers?size=50"),
            page("/api/products?size=50"),
            page("/api/stock-movements?size=50"),
            page("/api/sales-orders?size=50"),
            request("/api/reports/sales-summary"),
            request("/api/reports/stock-summary"),
            request("/api/reports/top-products"),
            page("/api/audit-events?size=50"),
        ]);

        renderCustomers(customers);
        renderProducts(products);
        renderStock(stock);
        renderOrders(orders);
        renderDashboard(sales, stockSummary, topProducts);
        renderAudit(audit);
        setMessage("Dados atualizados.");
    } catch (error) {
        setMessage(error.message, true);
    }
}

async function page(url) {
    const response = await request(url);
    return response.content || [];
}

async function request(url, options = {}) {
    const headers = {
        "Accept": "application/json",
    };

    if (options.body) {
        headers["Content-Type"] = "application/json";
    }

    if (options.auth !== false && state.token) {
        headers.Authorization = `Bearer ${state.token}`;
    }

    const response = await fetch(url, {
        method: options.method || "GET",
        headers,
        body: options.body ? JSON.stringify(options.body) : undefined,
    });

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error(errorMessage(data, response.status));
    }

    return data;
}

function formDataJson(form) {
    const data = Object.fromEntries(new FormData(form).entries());

    Object.keys(data).forEach((key) => {
        if (["price"].includes(key)) {
            data[key] = decimalValue(data[key]);
        }

        if (["stockQuantity", "minimumStock", "quantity", "productId"].includes(key)) {
            data[key] = numberValue(data[key]);
        }
    });

    return data;
}

function decimalValue(value) {
    return Number.parseFloat(value || "0");
}

function numberValue(value) {
    return Number.parseInt(value || "0", 10);
}

function setMessage(message, error = false) {
    globalMessage.textContent = message;
    globalMessage.classList.toggle("danger", error);
}

function errorMessage(data, status) {
    if (data?.message) {
        return data.message;
    }

    return `Operacao nao concluida. Codigo ${status}.`;
}

function renderCustomers(customers) {
    renderRows("#customersTable", customers, (customer) => `
        <tr>
            <td>${customer.id}</td>
            <td>${escapeHtml(customer.name)}</td>
            <td>${escapeHtml(customer.email)}</td>
            <td><span class="badge">${customer.active ? "Ativo" : "Inativo"}</span></td>
        </tr>
    `);
}

function renderProducts(products) {
    renderRows("#productsTable", products, (product) => `
        <tr>
            <td>${product.id}</td>
            <td>${escapeHtml(product.name)}${product.lowStock ? " <span class=\"danger\">baixo</span>" : ""}</td>
            <td>${escapeHtml(product.sku)}</td>
            <td>${money.format(product.price || 0)}</td>
            <td>${product.stockQuantity}</td>
        </tr>
    `);
}

function renderStock(movements) {
    renderRows("#stockTable", movements, (movement) => `
        <tr>
            <td>${escapeHtml(movement.productName)}</td>
            <td><span class="badge">${movement.type === "INBOUND" ? "Entrada" : "Saida"}</span></td>
            <td>${movement.quantity}</td>
            <td>${escapeHtml(movement.reason)}</td>
        </tr>
    `);
}

function renderOrders(orders) {
    renderRows("#ordersTable", orders, (order) => `
        <tr>
            <td>${order.id}</td>
            <td>${escapeHtml(order.customerName)}</td>
            <td><span class="badge">${statusLabel(order.status)}</span></td>
            <td>${money.format(order.totalAmount || 0)}</td>
            <td>${order.status === "CREATED" ? `<button class="row-action" type="button" onclick="confirmOrder(${order.id})">Confirmar</button>` : ""}</td>
        </tr>
    `);
}

function renderDashboard(sales, stockSummary, topProducts) {
    document.querySelector("#confirmedOrders").textContent = sales.confirmedOrders || 0;
    document.querySelector("#confirmedRevenue").textContent = money.format(sales.confirmedRevenue || 0);
    document.querySelector("#activeProducts").textContent = stockSummary.activeProducts || 0;
    document.querySelector("#lowStockProducts").textContent = stockSummary.lowStockProducts || 0;

    renderRows("#topProductsTable", topProducts || [], (product) => `
        <tr>
            <td>${escapeHtml(product.name)}</td>
            <td>${escapeHtml(product.sku)}</td>
            <td>${product.quantitySold}</td>
            <td>${money.format(product.revenue || 0)}</td>
        </tr>
    `);
}

function renderAudit(events) {
    renderRows("#auditTable", events, (event) => `
        <tr>
            <td>${formatDate(event.occurredAt)}</td>
            <td>${escapeHtml(event.username)}</td>
            <td>${escapeHtml(event.action)}</td>
            <td>${escapeHtml(event.entityType)} #${event.entityId}</td>
            <td>${escapeHtml(event.description)}</td>
        </tr>
    `);
}

async function confirmOrder(id) {
    try {
        await request(`/api/sales-orders/${id}/confirm`, { method: "PATCH" });
        setMessage("Pedido confirmado.");
        await loadAll();
    } catch (error) {
        setMessage(error.message, true);
    }
}

function renderRows(selector, rows, template) {
    const target = document.querySelector(selector);

    if (!rows.length) {
        target.innerHTML = `<tr><td colspan="6">Nenhum registro encontrado.</td></tr>`;
        return;
    }

    target.innerHTML = rows.map(template).join("");
}

function statusLabel(status) {
    const labels = {
        CREATED: "Criado",
        CONFIRMED: "Confirmado",
        CANCELED: "Cancelado",
    };

    return labels[status] || status;
}

function formatDate(value) {
    if (!value) {
        return "";
    }

    return new Intl.DateTimeFormat("pt-BR", {
        dateStyle: "short",
        timeStyle: "short",
    }).format(new Date(value));
}

function escapeHtml(value) {
    return String(value || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
