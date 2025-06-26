<script>
export default {
    name: 'AppFooter',
};
</script>

<template>
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-links">
                <a href="https://github.com/IsidoraSRM/Lab2-TBD-Grupo3.git" target="_blank" class="github-link">
                    <span>Repositorio de GitHub	</span>
                </a>
                <a href="#" class="footer-link">Términos</a>
                <a href="#" class="footer-link">Privacidad</a>
                <a href="#" class="footer-link">Contacto</a>
            </div>
            <div class="footer-bottom">
                <p>© 2024 DocDelivery. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>
</template>

<style scoped>
.footer {
    background-color: var(--bg-secondary);
    color: var(--text-primary);
    padding: 1.25rem 0;
    width: 100%;
    min-height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-top: 1px solid var(--border-blue);
}

.footer-content {
    width: 100%;
    max-width: 1200px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 1rem;
    padding: 0 2rem;
}

.footer-links {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 2.5rem;
    flex-wrap: wrap;
}

.footer-link, .github-link {
    color: var(--text-secondary);
    text-decoration: none;
    font-size: 0.9rem;
    transition: color 0.2s ease;
    padding: 0.5rem;
}

.github-link {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: var(--blue-neon);
}

.footer-link:hover, .github-link:hover {
    color: var(--text-primary);
}

.footer-bottom {
    text-align: center;
    color: var(--text-secondary);
    font-size: 0.85rem;
}

.footer-bottom p {
    margin: 0;
}

@media (max-width: 768px) {
    .footer {
        padding: 1rem 0;
    }

    .footer-links {
        gap: 1.5rem;
    }

    .footer-content {
        padding: 0 1rem;
    }
}
</style>
