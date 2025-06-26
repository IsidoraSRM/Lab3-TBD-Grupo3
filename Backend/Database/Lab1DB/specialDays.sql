-- Días Especiales de Chile (2025-2030)
-- special_discount: 
--   1.0 = Precio normal (sin cambio)
--   1.5 = +50% recargo (ej. Navidad)
--   0.5 = 50% descuento (ej. Día del Padre)

INSERT INTO special_day (date, name, special_discount) VALUES
  -- 2025
  ('2025-01-01', 'Año Nuevo', 1.3),
  ('2025-04-18', 'Viernes Santo', 1.4),
  ('2025-04-19', 'Sábado Santo', 1.2),
  ('2025-05-01', 'Día del Trabajo', 1.3),
  ('2025-05-21', 'Día de las Glorias Navales', 1.2),
  ('2025-06-15', 'Día del Padre', 0.5),
  ('2025-06-29', 'San Pedro y San Pablo', 1.0),
  ('2025-07-16', 'Día de la Virgen del Carmen', 1.1),
  ('2025-09-18', 'Fiestas Patrias', 1.5),
  ('2025-09-19', 'Día de las Glorias del Ejército', 1.4),
  ('2025-10-12', 'Encuentro de Dos Mundos', 1.0),
  ('2025-10-31', 'Día de las Iglesias Evangélicas', 1.0),
  ('2025-11-01', 'Día de Todos los Santos', 1.2),
  ('2025-12-08', 'Inmaculada Concepción', 1.1),
  ('2025-12-25', 'Navidad', 1.5),

  -- 2026 (los feriados móviles cambian de fecha)
  ('2026-01-01', 'Año Nuevo', 1.3),
  ('2026-04-03', 'Viernes Santo', 1.4),
  ('2026-04-04', 'Sábado Santo', 1.2),
  ('2026-05-01', 'Día del Trabajo', 1.3),
  ('2026-05-21', 'Día de las Glorias Navales', 1.2),
  ('2026-06-21', 'Día del Padre', 0.5),
  ('2026-06-29', 'San Pedro y San Pablo', 1.0),
  ('2026-07-16', 'Día de la Virgen del Carmen', 1.1),
  ('2026-09-18', 'Fiestas Patrias', 1.5),
  ('2026-09-19', 'Día de las Glorias del Ejército', 1.4),
  ('2026-10-12', 'Encuentro de Dos Mundos', 1.0),
  ('2026-10-31', 'Día de las Iglesias Evangélicas', 1.0),
  ('2026-11-01', 'Día de Todos los Santos', 1.2),
  ('2026-12-08', 'Inmaculada Concepción', 1.1),
  ('2026-12-25', 'Navidad', 1.5),

  -- 2027
  ('2027-01-01', 'Año Nuevo', 1.3),
  ('2027-03-26', 'Viernes Santo', 1.4),
  ('2027-03-27', 'Sábado Santo', 1.2),
  ('2027-05-01', 'Día del Trabajo', 1.3),
  ('2027-05-21', 'Día de las Glorias Navales', 1.2),
  ('2027-06-20', 'Día del Padre', 0.5),
  ('2027-06-29', 'San Pedro y San Pablo', 1.0),
  ('2027-07-16', 'Día de la Virgen del Carmen', 1.1),
  ('2027-09-18', 'Fiestas Patrias', 1.5),
  ('2027-09-19', 'Día de las Glorias del Ejército', 1.4),
  ('2027-10-12', 'Encuentro de Dos Mundos', 1.0),
  ('2027-10-31', 'Día de las Iglesias Evangélicas', 1.0),
  ('2027-11-01', 'Día de Todos los Santos', 1.2),
  ('2027-12-08', 'Inmaculada Concepción', 1.1),
  ('2027-12-25', 'Navidad', 1.5),

  -- 2028-2030 (patrón similar, ajustando feriados móviles)
  -- ... (continuaría con los años restantes)
  
  -- Ejemplo de días con descuentos promocionales (no son feriados)
  ('2025-02-14', 'Día de San Valentín', 0.8),  -- 20% descuento
  ('2025-05-10', 'Día de la Madre', 0.7),      -- 30% descuento
  ('2026-08-15', 'Aniversario Local', 0.6);    -- 40% descuento