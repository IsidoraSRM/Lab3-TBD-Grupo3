<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1 class="hero-title">Entregas rápidas y seguras</h1>
        <p class="hero-subtitle">La plataforma de delivery que conecta a las personas con sus necesidades</p>
        <div class="cta-buttons">
          <router-link to="/login" class="cta-button primary">Iniciar Sesión</router-link>
          <router-link to="/register" class="cta-button secondary">Registrarse</router-link>
        </div>
      </div>
      <div class="hero-stats">
        <div class="stat-item">
          <span class="stat-number">2K+</span>
          <span class="stat-label">Entregas</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">500+</span>
          <span class="stat-label">Clientes</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">50+</span>
          <span class="stat-label">Ciudades</span>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <h2 class="section-title">¿Por qué elegirnos?</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">🚀</div>
          <h3>Entregas Rápidas</h3>
          <p>Servicio de entrega optimizado para llegar a tiempo</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🔒</div>
          <h3>Seguridad</h3>
          <p>Tus envíos protegidos de principio a fin</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📱</div>
          <h3>Fácil de Usar</h3>
          <p>Interfaz intuitiva para todos los usuarios</p>
        </div>
      </div>
    </section>

    <!-- Image Carousel Section -->
    <section class="carousel-section">
      <h2 class="section-title">Nuestros Servicios</h2>
      <div class="carousel-container">
        <div class="carousel-track">
          <div class="carousel-card">
            <img src="@/assets/image1.jpeg" alt="Servicio 1">
            <div class="carousel-content">
              <h3>Entregas Locales</h3>
              <p>Conectamos negocios locales con sus clientes</p>
            </div>
          </div>
          <div class="carousel-card">
            <img src="@/assets/image2.jpeg" alt="Servicio 2">
            <div class="carousel-content">
              <h3>Envíos Express</h3>
              <p>Servicio premium para entregas urgentes</p>
            </div>
          </div>
          <div class="carousel-card">
            <img src="@/assets/image3.jpeg" alt="Servicio 3">
            <div class="carousel-content">
              <h3>Seguimiento en Tiempo Real</h3>
              <p>Monitorea tus envíos en todo momento</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home {
  background-color: var(--bg-primary);
  min-height: 100vh;
  padding: 0;
}

/* Hero Section */
.hero {
  background-color: var(--bg-secondary);
  min-height: 80vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 4rem 2rem;
  position: relative;
  overflow: hidden;
  border-bottom: 1px solid var(--border-blue);
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at top right, var(--blue-glow) 0%, transparent 70%);
  opacity: 0.1;
}

.hero-content {
  text-align: center;
  max-width: 800px;
  z-index: 1;
}

.hero-title {
  font-size: 4rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 1.5rem;
  line-height: 1.2;
  letter-spacing: -1px;
}

.hero-subtitle {
  font-size: 1.5rem;
  color: var(--text-secondary);
  margin-bottom: 3rem;
  line-height: 1.5;
}

.cta-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  margin-bottom: 4rem;
}

.cta-button {
  padding: 1rem 2.5rem;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
}

.cta-button.primary {
  background-color: var(--primary-blue);
  color: var(--text-primary);
  border: none;
}

.cta-button.primary:hover {
  background-color: var(--primary-blue-hover);
  transform: translateY(-2px);
}

.cta-button.secondary {
  background-color: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border-blue);
}

.cta-button.secondary:hover {
  background-color: var(--border-blue);
  transform: translateY(-2px);
}

/* Stats Section */
.hero-stats {
  display: flex;
  gap: 4rem;
  margin-top: 2rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--primary-blue);
  margin-bottom: 0.5rem;
}

.stat-label {
  color: var(--text-secondary);
  font-size: 1rem;
}

/* Features Section */
.features {
  padding: 6rem 2rem;
  background-color: var(--bg-primary);
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  color: var(--text-primary);
  margin-bottom: 4rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background-color: var(--bg-secondary);
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  border: 1px solid var(--border-blue);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  color: var(--text-primary);
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.feature-card p {
  color: var(--text-secondary);
  line-height: 1.6;
}

/* Carousel Section */
.carousel-section {
  padding: 6rem 2rem;
  background-color: var(--bg-secondary);
  border-top: 1px solid var(--border-blue);
}

.carousel-container {
  max-width: 1200px;
  margin: 0 auto;
  overflow: hidden;
}

.carousel-track {
  display: flex;
  gap: 2rem;
  padding: 2rem 0;
}

.carousel-card {
  flex: 0 0 calc(33.333% - 1.33rem);
  background-color: var(--bg-primary);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid var(--border-blue);
}

.carousel-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.carousel-content {
  padding: 1.5rem;
}

.carousel-content h3 {
  color: var(--text-primary);
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
}

.carousel-content p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .hero {
    min-height: 60vh;
    padding: 3rem 1rem;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .hero-stats {
    flex-direction: column;
    gap: 2rem;
  }

  .cta-buttons {
    flex-direction: column;
    gap: 1rem;
  }

  .features, .carousel-section {
    padding: 4rem 1rem;
  }

  .carousel-track {
    flex-direction: column;
  }

  .carousel-card {
    flex: 0 0 100%;
  }
}
</style>