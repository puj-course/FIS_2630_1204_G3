/**
 * =========================================================================
 * WISETRIP — MAPA RÁPIDO DE PENDIENTES PARA EL EQUIPO
 * =========================================================================
 * Este archivo es funcional de punta a punta con datos y almacenamiento
 * simulados (todo vive en memoria vía useState). Busca "TODO" en este
 * archivo para encontrar cada punto de integración. Resumen:
 *
 *   1. TODO BACKEND — Registro/Login          -> junto a RegisterScreen
 *   2. TODO API — Actividades/lugares          -> junto a ACTIVITIES
 *   3. TODO API — Clima                        -> sección "Próximas integraciones"
 *   4. TODO API — Mensajería (Telegram/Email)   -> sección "Próximas integraciones"
 *   5. TODO API — Pagos                         -> sección "Próximas integraciones"
 *   6. TODO NEGOCIO — Distribución presupuesto  -> junto a BUDGET_SPLIT
 *   7. TODO IDENTIDAD — Validación real de documento -> junto a RegisterScreen
 *
 * Flujo de pantallas: Portada (landing) -> Crear cuenta / Iniciar sesión ->
 * Preferencias -> Fechas -> Presupuesto -> Confirmación -> Dashboard.
 *
 * Ninguna de estas integraciones es necesaria para que la app funcione
 * en esta versión: todo el flujo (registro, login, preferencias, fechas,
 * presupuesto y dashboard) ya opera con datos reales del usuario.
 * =========================================================================
 */
import React, { useState, useMemo } from "react";
import {
  Plane, MapPin, Calendar, Wallet, Mountain, Landmark, UtensilsCrossed,
  Palmtree, Check, Eye, EyeOff, ArrowRight, ArrowLeft, AlertCircle,
  CloudSun, MessageCircle, CreditCard, Compass, Sparkles, LogOut, Ticket
} from "lucide-react";

// ---------- Tokens ----------
const COLORS = {
  ink: "#0E3A53",
  inkDeep: "#0A2A3E",
  turquoise: "#1BA3A0",
  turquoiseDeep: "#128784",
  coral: "#E8623D",
  sand: "#F6F1E6",
  sandDeep: "#EDE4D3",
  white: "#FFFFFF",
  gray: "#5B6B75",
  grayLight: "#93A3AC",
  line: "#D8CFBC",
};

const PREFERENCES = [
  { id: "Aventura", label: "Aventura", icon: Mountain, hint: "Adrenalina y naturaleza" },
  { id: "Cultura", label: "Cultura", icon: Landmark, hint: "Historia y patrimonio" },
  { id: "Gastronomía", label: "Gastronomía", icon: UtensilsCrossed, hint: "Sabores locales" },
  { id: "Descanso", label: "Descanso", icon: Palmtree, hint: "Relax y desconexión" },
];

/**
 * =====================================================================
 * TODO API — ACTIVIDADES / LUGARES TURÍSTICOS
 * =====================================================================
 * Estos datos son simulados. Deben reemplazarse por una llamada real
 * a un servicio de actividades/turismo (ej. Google Places, Amadeus
 * Travel API, GetYourGuide, TripAdvisor Content API, etc).
 *
 * Pasos sugeridos para quien implemente esto:
 * 1. Crear una función async `fetchActividades(preferencias, destino)`
 *    que reciba el arreglo de preferencias seleccionadas por el
 *    usuario (ej. ["Cultura", "Gastronomía"]) y el destino/ubicación.
 * 2. Llamar al endpoint real de la API elegida, mapeando cada
 *    categoría del negocio (Aventura, Cultura, Gastronomía, Descanso)
 *    a las categorías/tags que use la API externa.
 * 3. Normalizar la respuesta al mismo shape que usa este objeto:
 *    { name, place, tag } — así el componente <DashboardScreen />
 *    no necesita cambios, solo la fuente de datos.
 * 4. Sustituir el uso de la constante ACTIVITIES en DashboardScreen
 *    por el resultado de fetchActividades(), manejando estados de
 *    carga (loading) y error (fallback a estos datos simulados si
 *    la API falla).
 * 5. Guardar la API key en variables de entorno, nunca en el código.
 * =====================================================================
 */
const ACTIVITIES = {
  Aventura: [
    { name: "Caminata al volcán Pacaya", place: "Antigua, Guatemala", tag: "Trekking" },
    { name: "Rafting en el río Pacuare", place: "Turrialba, Costa Rica", tag: "Río" },
    { name: "Parapente en Roldanillo", place: "Valle del Cauca, Colombia", tag: "Aéreo" },
    { name: "Buceo en el Parque Tayrona", place: "Magdalena, Colombia", tag: "Buceo" },
  ],
  Cultura: [
    { name: "Recorrido por Ciudad Perdida", place: "Sierra Nevada, Colombia", tag: "Patrimonio" },
    { name: "Museo Larco", place: "Lima, Perú", tag: "Museo" },
    { name: "Free tour Centro Histórico", place: "Cartagena, Colombia", tag: "Historia" },
    { name: "Ruinas de Tulum", place: "Quintana Roo, México", tag: "Arqueología" },
  ],
  Gastronomía: [
    { name: "Ruta de arepas y fritos", place: "Medellín, Colombia", tag: "Callejera" },
    { name: "Clase de cocina peruana", place: "Lima, Perú", tag: "Taller" },
    { name: "Cata de café de origen", place: "Eje Cafetero, Colombia", tag: "Café" },
    { name: "Mercado de mariscos", place: "Cartagena, Colombia", tag: "Mercado" },
  ],
  Descanso: [
    { name: "Playa Blanca al atardecer", place: "Islas del Rosario, Colombia", tag: "Playa" },
    { name: "Spa de aguas termales", place: "Coconuco, Colombia", tag: "Termales" },
    { name: "Mirador con hamacas", place: "Tayrona, Colombia", tag: "Naturaleza" },
    { name: "Crucero al atardecer", place: "Cartagena, Colombia", tag: "Mar" },
  ],
};

/**
 * TODO NEGOCIO/API — DISTRIBUCIÓN DE PRESUPUESTO
 * Hoy los porcentajes son fijos (35/20/25/15/5). A futuro esto podría
 * calcularse dinámicamente según el destino real (costo de vida),
 * duración del viaje y precios obtenidos de las APIs de hospedaje/
 * transporte. Por ahora es un valor de referencia razonable, no
 * requiere API para la v1.
 */
const BUDGET_SPLIT = [
  { key: "Hospedaje", pct: 0.35 },
  { key: "Transporte", pct: 0.20 },
  { key: "Alimentación", pct: 0.25 },
  { key: "Actividades", pct: 0.15 },
  { key: "Otros gastos", pct: 0.05 },
];

const STEPS = ["Preferencias", "Fechas", "Presupuesto", "Tu plan"];

function formatMoney(amount, currency) {
  if (currency === "COP") {
    return "$" + Math.round(amount).toLocaleString("es-CO") + " COP";
  }
  return "$" + amount.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 }) + " USD";
}

function emailValid(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// ---------- Small UI atoms ----------
function FieldError({ children }) {
  if (!children) return null;
  return (
    <div className="flex items-start gap-2 mt-1.5 text-sm" style={{ color: COLORS.coral }}>
      <AlertCircle size={15} style={{ marginTop: 2, flexShrink: 0 }} />
      <span>{children}</span>
    </div>
  );
}

function TextField({ label, type = "text", value, onChange, error, placeholder, rightIcon, onRightIconClick }) {
  return (
    <div className="mb-4">
      <label className="block text-sm font-semibold mb-1.5" style={{ color: COLORS.ink }}>{label}</label>
      <div className="relative">
        <input
          type={type}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder}
          className="w-full rounded-lg px-3.5 py-2.5 text-sm outline-none transition-all"
          style={{
            border: `1.5px solid ${error ? COLORS.coral : COLORS.line}`,
            background: COLORS.white,
            color: COLORS.inkDeep,
          }}
        />
        {rightIcon && (
          <button
            type="button"
            onClick={onRightIconClick}
            className="absolute right-3 top-1/2 -translate-y-1/2"
            style={{ color: COLORS.grayLight }}
          >
            {rightIcon}
          </button>
        )}
      </div>
      <FieldError>{error}</FieldError>
    </div>
  );
}

function Logo({ dark }) {
  return (
    <div className="flex items-center gap-2">
      <div
        className="flex items-center justify-center rounded-full"
        style={{ width: 34, height: 34, background: dark ? COLORS.turquoise : COLORS.white }}
      >
        <Compass size={18} color={dark ? COLORS.white : COLORS.turquoise} />
      </div>
      <span
        className="text-xl font-bold tracking-tight"
        style={{ color: dark ? COLORS.white : COLORS.ink, fontFamily: "'Fraunces', serif" }}
      >
        WiseTrip
      </span>
    </div>
  );
}

function ProgressBar({ currentIndex }) {
  return (
    <div className="w-full max-w-2xl mx-auto mb-8 px-4">
      <div className="flex items-center">
        {STEPS.map((step, i) => (
          <React.Fragment key={step}>
            <div className="flex flex-col items-center" style={{ minWidth: 64 }}>
              <div
                className="flex items-center justify-center rounded-full font-bold text-sm transition-all"
                style={{
                  width: 32, height: 32,
                  background: i < currentIndex ? COLORS.turquoise : i === currentIndex ? COLORS.coral : COLORS.white,
                  color: i <= currentIndex ? COLORS.white : COLORS.grayLight,
                  border: i === currentIndex ? `2px solid ${COLORS.coral}` : `1.5px solid ${COLORS.line}`,
                }}
              >
                {i < currentIndex ? <Check size={16} /> : i + 1}
              </div>
              <span
                className="text-xs mt-1.5 text-center font-medium"
                style={{ color: i === currentIndex ? COLORS.ink : COLORS.grayLight }}
              >
                {step}
              </span>
            </div>
            {i < STEPS.length - 1 && (
              <div className="flex-1 h-0.5 mb-5" style={{ background: i < currentIndex ? COLORS.turquoise : COLORS.line }} />
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
}

function FontImport() {
  return (
    <style>{`
      @import url('https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,500;9..144,600;9..144,700&family=Manrope:wght@400;500;600;700;800&family=IBM+Plex+Mono:wght@500;600&display=swap');
    `}</style>
  );
}

function AuthShell({ children }) {
  return (
    <div className="min-h-screen w-full flex" style={{ background: COLORS.sand, fontFamily: "'Manrope', sans-serif" }}>
      <FontImport />
      <div
        className="hidden md:flex md:w-2/5 flex-col justify-between p-10 relative overflow-hidden"
        style={{ background: `linear-gradient(160deg, ${COLORS.ink}, ${COLORS.inkDeep})` }}
      >
        <Logo dark />
        <div>
          <p className="text-3xl leading-snug font-semibold" style={{ color: COLORS.white, fontFamily: "'Fraunces', serif" }}>
            Diseña tu próximo viaje por Latinoamérica, a tu manera.
          </p>
          <p className="mt-4 text-sm" style={{ color: "#B9CBD3" }}>
            Preferencias, fechas y presupuesto: tú decides. WiseTrip organiza el resto.
          </p>
        </div>
        <div className="flex items-center gap-2" style={{ color: "#7FA9AF" }}>
          <MapPin size={16} /><span className="text-xs">Colombia · Perú · México · Costa Rica · Guatemala</span>
        </div>
        <div
          className="absolute -bottom-24 -right-24 rounded-full opacity-20"
          style={{ width: 320, height: 320, background: COLORS.turquoise }}
        />
      </div>
      <div className="flex-1 flex items-center justify-center p-6">
        <div className="w-full max-w-sm">{children}</div>
      </div>
    </div>
  );
}

function Card({ children, className = "", style = {} }) {
  return (
    <div
      className={`rounded-2xl ${className}`}
      style={{ background: COLORS.white, border: `1px solid ${COLORS.line}`, boxShadow: "0 1px 2px rgba(14,58,83,0.04)", ...style }}
    >
      {children}
    </div>
  );
}

function PrimaryButton({ children, onClick, disabled, full, type = "button" }) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`inline-flex items-center justify-center gap-2 rounded-lg font-semibold text-sm px-5 py-2.5 transition-all ${full ? "w-full" : ""}`}
      style={{
        background: disabled ? COLORS.grayLight : COLORS.coral,
        color: COLORS.white,
        cursor: disabled ? "not-allowed" : "pointer",
        opacity: disabled ? 0.7 : 1,
      }}
    >
      {children}
    </button>
  );
}

function GhostButton({ children, onClick }) {
  return (
    <button
      onClick={onClick}
      className="inline-flex items-center justify-center gap-2 rounded-lg font-semibold text-sm px-5 py-2.5"
      style={{ background: "transparent", color: COLORS.ink, border: `1.5px solid ${COLORS.line}` }}
    >
      {children}
    </button>
  );
}

function SelectField({ label, value, onChange, options, error }) {
  return (
    <div className="mb-4">
      <label className="block text-sm font-semibold mb-1.5" style={{ color: COLORS.ink }}>{label}</label>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-lg px-3.5 py-2.5 text-sm outline-none"
        style={{ border: `1.5px solid ${error ? COLORS.coral : COLORS.line}`, background: COLORS.white, color: COLORS.inkDeep }}
      >
        <option value="">Selecciona una opción</option>
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
      <FieldError>{error}</FieldError>
    </div>
  );
}

function BackToLanding({ onClick }) {
  return (
    <button onClick={onClick} className="flex items-center gap-1.5 text-sm font-medium mb-5" style={{ color: COLORS.gray }}>
      <ArrowLeft size={15} /> Volver al inicio
    </button>
  );
}


/**
 * =====================================================================
 * TODO BACKEND — REGISTRO / AUTENTICACIÓN
 * componente raíz) y se pierde al recargar la página. Para producción:
 *
 * 1. Crear un backend (Node/Express, Firebase Auth, Supabase, etc.)
 *    con endpoints:
 *      POST /api/auth/register  { email, password } -> 201 / 409 si ya existe
 *      POST /api/auth/login     { email, password } -> 200 + token / 401
 * 2. En `onRegister` (más abajo, dentro del componente RegisterScreen)
 *    reemplazar `onRegister({ email, password })` por un fetch/axios
 *    POST a /api/auth/register. Mostrar `errors.email` si el backend
 *    responde que el correo ya existe.
 * 3. NUNCA guardar la contraseña en texto plano — el backend debe
 *    hashearla (bcrypt/argon2) antes de guardar en base de datos.
 * 4. En LoginScreen, reemplazar la búsqueda en el arreglo `users` por
 *    un fetch/axios POST a /api/auth/login, y guardar el token
 *    devuelto (ej. JWT) en memoria o en una cookie httpOnly segura.
 * 5. El botón "¿Olvidaste tu contraseña?" hoy solo simula un mensaje;
 *    debe llamar a un endpoint real que envíe un correo con enlace
 *    de recuperación (ver sección de Mensajería más abajo).
 * =====================================================================
 */

// ---------- Screens ----------
const DOCUMENT_TYPES = [
  { value: "CC", label: "Cédula de ciudadanía" },
  { value: "CE", label: "Cédula de extranjería" },
  { value: "TI", label: "Tarjeta de identidad" },
  { value: "PA", label: "Pasaporte" },
];

function calculateAge(birthDateStr) {
  const today = new Date();
  const birth = new Date(birthDateStr);
  let age = today.getFullYear() - birth.getFullYear();
  const monthDiff = today.getMonth() - birth.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) age--;
  return age;
}

function RegisterScreen({ users, onRegister, goLogin, goLanding }) {
  const [fullName, setFullName] = useState("");
  const [docType, setDocType] = useState("");
  const [docNumber, setDocNumber] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);

  const submit = () => {
    const errs = {};
    if (!fullName.trim()) errs.fullName = "Ingresa tu nombre completo.";
    else if (fullName.trim().length < 3) errs.fullName = "El nombre es demasiado corto.";

    if (!docType) errs.docType = "Selecciona un tipo de documento.";

    if (!docNumber.trim()) errs.docNumber = "Ingresa tu número de documento.";
    // TODO IDENTIDAD: esta validación solo revisa formato (5-15 dígitos) y
    // que no se repita en memoria. Para producción, considerar validar el
    // documento contra un servicio real (ej. Registraduría/RENIEC según país)
    // antes de aceptar el registro.
    else if (!/^\d{5,15}$/.test(docNumber.trim())) errs.docNumber = "El documento debe tener entre 5 y 15 dígitos.";
    else if (users.some((u) => u.docNumber === docNumber.trim())) errs.docNumber = "Ya existe una cuenta con este documento.";

    if (!birthDate) errs.birthDate = "Ingresa tu fecha de nacimiento.";
    else {
      const age = calculateAge(birthDate);
      if (new Date(birthDate) > new Date()) errs.birthDate = "La fecha de nacimiento no puede ser futura.";
      else if (age < 18) errs.birthDate = "Debes ser mayor de 18 años para crear una cuenta en WiseTrip.";
      else if (age > 120) errs.birthDate = "Ingresa una fecha de nacimiento válida.";
    }

    if (!email.trim()) errs.email = "Ingresa tu correo electrónico.";
    else if (!emailValid(email)) errs.email = "El formato del correo no es válido.";
    else if (users.some((u) => u.email.toLowerCase() === email.trim().toLowerCase())) errs.email = "Ya existe una cuenta con este correo.";

    if (!password) errs.password = "Ingresa una contraseña.";
    else if (password.length < 6) errs.password = "La contraseña debe tener al menos 6 caracteres.";
    if (!confirm) errs.confirm = "Confirma tu contraseña.";
    else if (password && confirm && password !== confirm) errs.confirm = "Las contraseñas no coinciden.";

    setErrors(errs);
    if (Object.keys(errs).length === 0) {
      onRegister({
        fullName: fullName.trim(),
        docType,
        docNumber: docNumber.trim(),
        birthDate,
        email: email.trim(),
        password,
      });
      setSuccess(true);
    }
  };

  if (success) {
    return (
      <AuthShell>
        <div className="text-center">
          <div className="mx-auto mb-4 flex items-center justify-center rounded-full" style={{ width: 56, height: 56, background: "#E4F5F1" }}>
            <Check size={26} color={COLORS.turquoiseDeep} />
          </div>
          <h2 className="text-xl font-bold mb-2" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>
            ¡Cuenta creada con éxito!
          </h2>
          <p className="text-sm mb-6" style={{ color: COLORS.gray }}>
            Hola <strong>{fullName}</strong>, tu cuenta <strong>{email}</strong> ya está lista. Ahora inicia sesión para continuar con la planificación de tu viaje.
          </p>
          <PrimaryButton full onClick={goLogin}>
            Ir a iniciar sesión <ArrowRight size={16} />
          </PrimaryButton>
        </div>
      </AuthShell>
    );
  }

  return (
    <AuthShell>
      <div className="mb-6 md:hidden"><Logo /></div>
      {goLanding && <BackToLanding onClick={goLanding} />}
      <h2 className="text-2xl font-bold mb-1" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>Crea tu cuenta</h2>
      <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Empieza a planear tu próximo viaje en minutos.</p>

      <TextField label="Nombre completo" value={fullName} onChange={setFullName} error={errors.fullName} placeholder="Ej: Laura Gómez Pérez" />

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-4">
        <SelectField label="Tipo de documento" value={docType} onChange={setDocType} error={errors.docType} options={DOCUMENT_TYPES} />
        <TextField label="Número de documento" value={docNumber} onChange={(v) => setDocNumber(v.replace(/[^\d]/g, ""))} error={errors.docNumber} placeholder="Ej: 1020304050" />
      </div>

      <TextField label="Fecha de nacimiento" type="date" value={birthDate} onChange={setBirthDate} error={errors.birthDate} />

      <TextField label="Correo electrónico" type="email" value={email} onChange={setEmail} error={errors.email} placeholder="tucorreo@ejemplo.com" />
      <TextField
        label="Contraseña" type={showPw ? "text" : "password"} value={password} onChange={setPassword}
        error={errors.password} placeholder="Mínimo 6 caracteres"
        rightIcon={showPw ? <EyeOff size={16} /> : <Eye size={16} />} onRightIconClick={() => setShowPw(!showPw)}
      />
      <TextField
        label="Confirmar contraseña" type={showPw ? "text" : "password"} value={confirm} onChange={setConfirm}
        error={errors.confirm} placeholder="Repite tu contraseña"
      />

      <PrimaryButton full onClick={submit}>Crear cuenta</PrimaryButton>

      <p className="text-sm text-center mt-5" style={{ color: COLORS.gray }}>
        ¿Ya tienes cuenta?{" "}
        <button onClick={goLogin} className="font-semibold" style={{ color: COLORS.turquoiseDeep }}>Inicia sesión</button>
      </p>
    </AuthShell>
  );
}

function LoginScreen({ users, onLogin, goRegister, goLanding }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [recoveryMsg, setRecoveryMsg] = useState("");

  const submit = () => {
    if (!email.trim() || !password) {
      setError("Ingresa tu correo y contraseña.");
      return;
    }
    const found = users.find((u) => u.email.toLowerCase() === email.trim().toLowerCase() && u.password === password);
    if (found) {
      setError("");
      onLogin(found.email);
    } else {
      setError("Correo o contraseña incorrectos. Verifica tus datos.");
    }
  };

  return (
    <AuthShell>
      <div className="mb-6 md:hidden"><Logo /></div>
      {goLanding && <BackToLanding onClick={goLanding} />}
      <h2 className="text-2xl font-bold mb-1" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>Inicia sesión</h2>
      <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Bienvenido de nuevo a WiseTrip.</p>

      <TextField label="Correo electrónico" type="email" value={email} onChange={setEmail} placeholder="tucorreo@ejemplo.com" />
      <TextField
        label="Contraseña" type={showPw ? "text" : "password"} value={password} onChange={setPassword}
        placeholder="Tu contraseña"
        rightIcon={showPw ? <EyeOff size={16} /> : <Eye size={16} />} onRightIconClick={() => setShowPw(!showPw)}
      />
      <FieldError>{error}</FieldError>

      <div className="mt-4">
        <PrimaryButton full onClick={submit}>Iniciar sesión</PrimaryButton>
      </div>

      <div className="flex items-center justify-between mt-4 text-sm">
        <button
          onClick={() => setRecoveryMsg(email.trim() && emailValid(email) ? `Enviamos un enlace de recuperación (simulado) a ${email}.` : "Ingresa un correo válido para recuperar tu contraseña.")}
          style={{ color: COLORS.turquoiseDeep }} className="font-medium"
        >
          ¿Olvidaste tu contraseña?
        </button>
        <button onClick={goRegister} style={{ color: COLORS.ink }} className="font-semibold">
          Crear una cuenta
        </button>
      </div>
      {recoveryMsg && (
        <div className="mt-3 text-sm rounded-lg px-3 py-2" style={{ background: "#E4F5F1", color: COLORS.turquoiseDeep }}>
          {recoveryMsg}
        </div>
      )}
    </AuthShell>
  );
}

/**
 * TODO: Portada / landing pública de WiseTrip. Punto de entrada de la
 * aplicación antes de autenticarse. A futuro se puede enriquecer con
 * imágenes reales de destinos (hoy solo usa íconos e ilustración de
 * color) o con un carrusel de testimonios.
 */
function LandingScreen({ goRegister, goLogin }) {
  return (
    <div className="min-h-screen w-full flex flex-col" style={{ background: COLORS.ink, fontFamily: "'Manrope', sans-serif" }}>
      <FontImport />
      <header className="w-full flex items-center justify-between px-6 py-5">
        <Logo dark />
      </header>

      <main className="flex-1 flex flex-col items-center justify-center px-6 py-10 relative overflow-hidden">
        <div
          className="absolute -top-32 -left-24 rounded-full opacity-20"
          style={{ width: 360, height: 360, background: COLORS.turquoise }}
        />
        <div
          className="absolute -bottom-32 -right-24 rounded-full opacity-20"
          style={{ width: 320, height: 320, background: COLORS.coral }}
        />

        <div className="relative z-10 max-w-xl text-center">
          <span
            className="inline-flex items-center gap-2 text-xs font-semibold tracking-widest px-3 py-1.5 rounded-full mb-6"
            style={{ background: "rgba(27,163,160,0.18)", color: COLORS.turquoise }}
          >
            <Compass size={13} /> PLANIFICACIÓN DE VIAJES INTELIGENTE
          </span>
          <h1
            className="text-3xl sm:text-4xl font-bold leading-tight mb-4"
            style={{ color: COLORS.white, fontFamily: "'Fraunces', serif" }}
          >
            Organiza tu próximo viaje por Latinoamérica a tu medida
          </h1>
          <p className="text-sm sm:text-base mb-9" style={{ color: "#B9CBD3" }}>
            Cuéntanos tus preferencias, fechas y presupuesto. WiseTrip arma una
            planificación inicial personalizada para ti.
          </p>

          <div className="flex flex-col sm:flex-row items-center justify-center gap-3">
            <div className="w-full sm:w-auto">
              <PrimaryButton full onClick={goRegister}>Crear cuenta <ArrowRight size={16} /></PrimaryButton>
            </div>
            <button
              onClick={goLogin}
              className="w-full sm:w-auto inline-flex items-center justify-center gap-2 rounded-lg font-semibold text-sm px-5 py-2.5"
              style={{ background: "transparent", color: COLORS.white, border: `1.5px solid rgba(255,255,255,0.35)` }}
            >
              Iniciar sesión
            </button>
          </div>

          <div className="flex items-center justify-center gap-6 mt-12 flex-wrap">
            {PREFERENCES.map(({ id, label, icon: Icon }) => (
              <div key={id} className="flex flex-col items-center gap-1.5">
                <div className="flex items-center justify-center rounded-full" style={{ width: 40, height: 40, background: "rgba(255,255,255,0.08)" }}>
                  <Icon size={18} color={COLORS.turquoise} />
                </div>
                <span className="text-xs" style={{ color: "#93A9B0" }}>{label}</span>
              </div>
            ))}
          </div>
        </div>
      </main>

      <footer className="flex items-center justify-center gap-2 pb-6" style={{ color: "#5F7A82" }}>
        <MapPin size={13} /><span className="text-xs">Colombia · Perú · México · Costa Rica · Guatemala</span>
      </footer>
    </div>
  );
}

function AppShell({ user, onLogout, children, stepIndex }) {
  return (
    <div className="min-h-screen" style={{ background: COLORS.sand, fontFamily: "'Manrope', sans-serif" }}>
      <FontImport />
      <header className="w-full flex items-center justify-between px-6 py-4" style={{ background: COLORS.ink }}>
        <Logo dark />
        <div className="flex items-center gap-4">
          <span className="text-sm hidden sm:inline" style={{ color: "#B9CBD3" }}>{user}</span>
          <button onClick={onLogout} className="flex items-center gap-1.5 text-sm font-medium" style={{ color: COLORS.white }}>
            <LogOut size={15} /> Salir
          </button>
        </div>
      </header>
      <main className="px-4 py-8">
        {stepIndex !== undefined && <ProgressBar currentIndex={stepIndex} />}
        {children}
      </main>
    </div>
  );
}

function PreferencesScreen({ selected, setSelected, onNext, user, onLogout }) {
  const [error, setError] = useState("");
  const toggle = (id) => {
    setError("");
    setSelected((prev) => (prev.includes(id) ? prev.filter((p) => p !== id) : [...prev, id]));
  };
  const next = () => {
    if (selected.length === 0) { setError("Selecciona al menos una preferencia para continuar."); return; }
    onNext();
  };
  return (
    <AppShell user={user} onLogout={onLogout} stepIndex={0}>
      <div className="max-w-2xl mx-auto">
        <Card style={{ padding: "2rem" }}>
          <h2 className="text-xl font-bold mb-1" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>
            ¿Qué tipo de experiencias buscas en tu viaje?
          </h2>
          <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Puedes elegir varias opciones. Usaremos esto para sugerirte actividades.</p>

          <div className="grid grid-cols-2 gap-3">
            {PREFERENCES.map(({ id, label, icon: Icon, hint }) => {
              const active = selected.includes(id);
              return (
                <button
                  key={id}
                  onClick={() => toggle(id)}
                  className="flex flex-col items-start gap-2 rounded-xl p-4 text-left transition-all"
                  style={{
                    border: `2px solid ${active ? COLORS.turquoise : COLORS.line}`,
                    background: active ? "#E4F5F1" : COLORS.white,
                  }}
                >
                  <div className="flex items-center justify-between w-full">
                    <Icon size={22} color={active ? COLORS.turquoiseDeep : COLORS.ink} />
                    {active && <Check size={16} color={COLORS.turquoiseDeep} />}
                  </div>
                  <span className="font-semibold text-sm" style={{ color: COLORS.ink }}>{label}</span>
                  <span className="text-xs" style={{ color: COLORS.gray }}>{hint}</span>
                </button>
              );
            })}
          </div>
          <FieldError>{error}</FieldError>

          <div className="flex justify-end mt-6">
            <PrimaryButton onClick={next}>Continuar <ArrowRight size={16} /></PrimaryButton>
          </div>
        </Card>
      </div>
    </AppShell>
  );
}

function DatesScreen({ dates, setDates, onNext, onBack, user, onLogout }) {
  const [error, setError] = useState("");
  const duration = useMemo(() => {
    if (!dates.start || !dates.end) return null;
    const d1 = new Date(dates.start);
    const d2 = new Date(dates.end);
    const diff = Math.round((d2 - d1) / (1000 * 60 * 60 * 24));
    return diff;
  }, [dates.start, dates.end]);

  const next = () => {
    if (!dates.start || !dates.end) { setError("Selecciona ambas fechas."); return; }
    if (duration <= 0) { setError("La fecha final debe ser posterior a la fecha inicial."); return; }
    setError("");
    onNext();
  };

  return (
    <AppShell user={user} onLogout={onLogout} stepIndex={1}>
      <div className="max-w-2xl mx-auto">
        <Card style={{ padding: "2rem" }}>
          <h2 className="text-xl font-bold mb-1 flex items-center gap-2" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>
            <Calendar size={20} color={COLORS.coral} /> ¿Cuándo viajas?
          </h2>
          <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Elige la fecha de inicio y finalización de tu viaje.</p>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <TextField label="Fecha de inicio" type="date" value={dates.start} onChange={(v) => setDates({ ...dates, start: v })} />
            <TextField label="Fecha de finalización" type="date" value={dates.end} onChange={(v) => setDates({ ...dates, end: v })} />
          </div>
          <FieldError>{error}</FieldError>

          {duration !== null && duration > 0 && (
            <div className="mt-2 rounded-xl px-4 py-3 flex items-center gap-3" style={{ background: COLORS.sandDeep }}>
              <div className="flex items-center justify-center rounded-full" style={{ width: 36, height: 36, background: COLORS.turquoise }}>
                <span className="text-white font-bold text-sm">{duration}</span>
              </div>
              <span className="text-sm" style={{ color: COLORS.ink }}>
                Tu viaje durará <strong>{duration} día{duration !== 1 ? "s" : ""}</strong>, calculado automáticamente.
              </span>
            </div>
          )}

          <div className="flex justify-between mt-6">
            <GhostButton onClick={onBack}><ArrowLeft size={16} /> Atrás</GhostButton>
            <PrimaryButton onClick={next}>Continuar <ArrowRight size={16} /></PrimaryButton>
          </div>
        </Card>
      </div>
    </AppShell>
  );
}

function BudgetScreen({ budget, setBudget, onNext, onBack, user, onLogout }) {
  const [error, setError] = useState("");

  const next = () => {
    const num = parseFloat(budget.amount);
    if (budget.amount === "" || budget.amount === null) { setError("El presupuesto no puede estar vacío."); return; }
    if (isNaN(num)) { setError("Ingresa un valor numérico válido."); return; }
    if (num <= 0) { setError("El presupuesto debe ser mayor que cero."); return; }
    setError("");
    onNext();
  };

  return (
    <AppShell user={user} onLogout={onLogout} stepIndex={2}>
      <div className="max-w-2xl mx-auto">
        <Card style={{ padding: "2rem" }}>
          <h2 className="text-xl font-bold mb-1 flex items-center gap-2" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>
            <Wallet size={20} color={COLORS.coral} /> ¿Cuál es tu presupuesto?
          </h2>
          <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Este monto se usará para preparar la distribución de tu viaje.</p>

          <div className="grid grid-cols-1 sm:grid-cols-[1fr_auto] gap-4 items-start">
            <TextField
              label="Monto disponible" type="number" value={budget.amount}
              onChange={(v) => setBudget({ ...budget, amount: v })}
              placeholder="Ej: 3000000"
            />
            <div className="mb-4">
              <label className="block text-sm font-semibold mb-1.5" style={{ color: COLORS.ink }}>Moneda</label>
              <select
                value={budget.currency}
                onChange={(e) => setBudget({ ...budget, currency: e.target.value })}
                className="rounded-lg px-3.5 py-2.5 text-sm outline-none w-full sm:w-28"
                style={{ border: `1.5px solid ${COLORS.line}`, background: COLORS.white, color: COLORS.inkDeep }}
              >
                <option value="COP">COP</option>
                <option value="USD">USD</option>
              </select>
            </div>
          </div>
          <FieldError>{error}</FieldError>

          {budget.amount && !isNaN(parseFloat(budget.amount)) && parseFloat(budget.amount) > 0 && (
            <div className="rounded-xl px-4 py-3" style={{ background: COLORS.sandDeep }}>
              <span className="text-sm" style={{ color: COLORS.ink }}>
                Presupuesto ingresado: <strong>{formatMoney(parseFloat(budget.amount), budget.currency)}</strong>
              </span>
            </div>
          )}

          <div className="flex justify-between mt-6">
            <GhostButton onClick={onBack}><ArrowLeft size={16} /> Atrás</GhostButton>
            <PrimaryButton onClick={next}>Continuar <ArrowRight size={16} /></PrimaryButton>
          </div>
        </Card>
      </div>
    </AppShell>
  );
}

function ReviewScreen({ preferences, dates, budget, duration, onCreate, onBack, user, onLogout }) {
  return (
    <AppShell user={user} onLogout={onLogout} stepIndex={2}>
      <div className="max-w-2xl mx-auto">
        <Card style={{ padding: "2rem" }}>
          <h2 className="text-xl font-bold mb-1 flex items-center gap-2" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>
            <Sparkles size={20} color={COLORS.coral} /> Confirma los datos de tu viaje
          </h2>
          <p className="text-sm mb-6" style={{ color: COLORS.gray }}>Revisa la información antes de generar tu planificación inicial.</p>

          <div className="space-y-3 mb-6">
            <div className="flex justify-between text-sm py-2" style={{ borderBottom: `1px solid ${COLORS.line}` }}>
              <span style={{ color: COLORS.gray }}>Preferencias</span>
              <span className="font-semibold text-right" style={{ color: COLORS.ink }}>{preferences.join(", ")}</span>
            </div>
            <div className="flex justify-between text-sm py-2" style={{ borderBottom: `1px solid ${COLORS.line}` }}>
              <span style={{ color: COLORS.gray }}>Fechas</span>
              <span className="font-semibold" style={{ color: COLORS.ink }}>{dates.start} → {dates.end}</span>
            </div>
            <div className="flex justify-between text-sm py-2" style={{ borderBottom: `1px solid ${COLORS.line}` }}>
              <span style={{ color: COLORS.gray }}>Duración</span>
              <span className="font-semibold" style={{ color: COLORS.ink }}>{duration} días</span>
            </div>
            <div className="flex justify-between text-sm py-2">
              <span style={{ color: COLORS.gray }}>Presupuesto</span>
              <span className="font-semibold" style={{ color: COLORS.ink }}>{formatMoney(parseFloat(budget.amount), budget.currency)}</span>
            </div>
          </div>

          <div className="flex justify-between">
            <GhostButton onClick={onBack}><ArrowLeft size={16} /> Atrás</GhostButton>
            <PrimaryButton onClick={onCreate}>Crear mi viaje <Plane size={16} /></PrimaryButton>
          </div>
        </Card>
      </div>
    </AppShell>
  );
}

function ApiPlaceholderCard({ icon: Icon, title, desc }) {
  return (
    <Card style={{ padding: "1.25rem" }}>
      <div className="flex items-start gap-3">
        <div className="flex items-center justify-center rounded-lg shrink-0" style={{ width: 38, height: 38, background: COLORS.sandDeep }}>
          <Icon size={18} color={COLORS.turquoiseDeep} />
        </div>
        <div>
          <div className="flex items-center gap-2">
            <h4 className="font-semibold text-sm" style={{ color: COLORS.ink }}>{title}</h4>
            <span className="text-xs font-medium px-2 py-0.5 rounded-full" style={{ background: "#FBEAE3", color: COLORS.coral }}>
              Próximamente vía API
            </span>
          </div>
          <p className="text-xs mt-1" style={{ color: COLORS.gray }}>{desc}</p>
        </div>
      </div>
    </Card>
  );
}

function DashboardScreen({ preferences, dates, budget, duration, user, userName, onLogout, onEdit }) {
  const total = parseFloat(budget.amount);
  const displayName = userName || user.split("@")[0];

  return (
    <AppShell user={user} onLogout={onLogout} stepIndex={3}>
      <div className="max-w-4xl mx-auto">
        {/* Boarding-pass style summary — signature element */}
        <div className="rounded-2xl overflow-hidden mb-8" style={{ background: COLORS.ink, boxShadow: "0 8px 24px rgba(14,58,83,0.18)" }}>
          <div className="flex flex-col sm:flex-row">
            <div className="flex-1 p-6">
              <div className="flex items-center gap-2 mb-4">
                <Ticket size={16} color={COLORS.turquoise} />
                <span className="text-xs tracking-widest font-semibold" style={{ color: "#7FA9AF" }}>ITINERARIO WISETRIP</span>
              </div>
              <h2 className="text-2xl font-bold mb-1" style={{ color: COLORS.white, fontFamily: "'Fraunces', serif" }}>
                Tu viaje está listo, {displayName}
              </h2>
              <p className="text-sm" style={{ color: "#B9CBD3" }}>{preferences.join(" · ")}</p>

              <div className="grid grid-cols-3 gap-4 mt-6" style={{ fontFamily: "'IBM Plex Mono', monospace" }}>
                <div>
                  <div className="text-xs" style={{ color: "#7FA9AF" }}>SALIDA</div>
                  <div className="text-lg font-bold" style={{ color: COLORS.white }}>{dates.start}</div>
                </div>
                <div>
                  <div className="text-xs" style={{ color: "#7FA9AF" }}>REGRESO</div>
                  <div className="text-lg font-bold" style={{ color: COLORS.white }}>{dates.end}</div>
                </div>
                <div>
                  <div className="text-xs" style={{ color: "#7FA9AF" }}>DURACIÓN</div>
                  <div className="text-lg font-bold" style={{ color: COLORS.white }}>{duration}D</div>
                </div>
              </div>
            </div>
            <div
              className="flex flex-col items-center justify-center p-6 relative"
              style={{ background: COLORS.turquoiseDeep, minWidth: 180, borderLeft: `2px dashed rgba(255,255,255,0.35)` }}
            >
              <span className="text-xs" style={{ color: "#DFF4F2" }}>PRESUPUESTO</span>
              <span className="text-xl font-bold text-center mt-1" style={{ color: COLORS.white, fontFamily: "'IBM Plex Mono', monospace" }}>
                {formatMoney(total, budget.currency)}
              </span>
              <button
                onClick={onEdit}
                className="mt-4 text-xs font-semibold px-3 py-1.5 rounded-full"
                style={{ background: "rgba(255,255,255,0.15)", color: COLORS.white }}
              >
                Editar datos
              </button>
            </div>
          </div>
        </div>

        {/* Budget breakdown */}
        <h3 className="text-lg font-bold mb-3" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>Distribución del presupuesto</h3>
        <div className="grid grid-cols-2 sm:grid-cols-5 gap-3 mb-8">
          {BUDGET_SPLIT.map(({ key, pct }) => (
            <Card key={key} style={{ padding: "1rem" }}>
              <div className="text-xs font-semibold mb-1" style={{ color: COLORS.gray }}>{key}</div>
              <div className="text-sm font-bold" style={{ color: COLORS.ink }}>{formatMoney(total * pct, budget.currency)}</div>
              <div className="mt-2 h-1.5 rounded-full" style={{ background: COLORS.sandDeep }}>
                <div className="h-1.5 rounded-full" style={{ width: `${pct * 100}%`, background: COLORS.turquoise }} />
              </div>
              <div className="text-xs mt-1" style={{ color: COLORS.grayLight }}>{Math.round(pct * 100)}%</div>
            </Card>
          ))}
        </div>

        {/* Suggested activities */}
        <h3 className="text-lg font-bold mb-3" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>Actividades sugeridas para ti</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
          {preferences.map((pref) => (
            <Card key={pref} style={{ padding: "1.25rem" }}>
              <div className="flex items-center gap-2 mb-3">
                {React.createElement(PREFERENCES.find((p) => p.id === pref).icon, { size: 18, color: COLORS.coral })}
                <h4 className="font-semibold text-sm" style={{ color: COLORS.ink }}>{pref}</h4>
              </div>
              <div className="space-y-2.5">
                {ACTIVITIES[pref].slice(0, 3).map((act) => (
                  <div key={act.name} className="flex items-start justify-between gap-2">
                    <div>
                      <div className="text-sm font-medium" style={{ color: COLORS.ink }}>{act.name}</div>
                      <div className="text-xs" style={{ color: COLORS.gray }}>{act.place}</div>
                    </div>
                    <span className="text-xs font-medium px-2 py-0.5 rounded-full whitespace-nowrap" style={{ background: COLORS.sandDeep, color: COLORS.turquoiseDeep }}>
                      {act.tag}
                    </span>
                  </div>
                ))}
              </div>
            </Card>
          ))}
        </div>

        {/*
          =====================================================================
          TODO API — INTEGRACIONES FUTURAS (CLIMA, MENSAJERÍA, PAGOS, ACTIVIDADES)
          =====================================================================
          Las 4 tarjetas de abajo son placeholders visuales. Cada una debe
          convertirse en un componente conectado a su respectiva API cuando
          se tengan las credenciales. Detalle por integración:

          1) CLIMA (icon={CloudSun})
             - API sugerida: OpenWeatherMap, WeatherAPI o AccuWeather.
             - Pasos: crear `WeatherCard` que reciba el destino y las fechas
               del viaje (`dates.start`, `dates.end`), llame al endpoint de
               pronóstico, y muestre temperatura, condición e ícono.
             - Reemplazar `<ApiPlaceholderCard icon={CloudSun} .../>` por
               `<WeatherCard destino={...} fechas={dates} />`.

          2) MENSAJERÍA (icon={MessageCircle})
             - API sugerida: Telegram Bot API + un proveedor de correo
               (SendGrid, Resend, Nodemailer + SMTP).
             - Pasos: crear un backend endpoint `POST /api/notify` que
               reciba { userId, tipo: "confirmacion" | "recordatorio",
               canal: "telegram" | "email" } y dispare el envío.
             - Se debe usar en: confirmación de cuenta creada, recuperación
               de contraseña y confirmación de "Crear mi viaje".

          3) PAGOS (icon={CreditCard})
             - API sugerida: Stripe, Wompi (Colombia) o Mercado Pago.
             - Pasos: crear pantalla `PaymentScreen` con formulario de
               tarjeta (usar SDK del proveedor, nunca capturar el número
               de tarjeta directamente en nuestro backend), y un endpoint
               `POST /api/payments/create-intent` que use el `budget.amount`
               ya calculado en esta pantalla como monto a cobrar.
             - Empezar en modo sandbox/test del proveedor antes de producción.

          4) ACTIVIDADES EN TIEMPO REAL (icon={Compass})
             - Ver el bloque de comentarios en la constante ACTIVITIES, al
               inicio del archivo, con el detalle completo de esta API.

          Mientras no haya credenciales, dejar `ApiPlaceholderCard` tal cual
          está: es la señal visual de "pendiente de API" para QA y para el
          resto del equipo.
          =====================================================================
        */}
        <h3 className="text-lg font-bold mb-3" style={{ color: COLORS.ink, fontFamily: "'Fraunces', serif" }}>Próximas integraciones</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-10">
          {/* TODO API CLIMA: reemplazar por <WeatherCard /> conectado a OpenWeatherMap/WeatherAPI */}
          <ApiPlaceholderCard icon={CloudSun} title="Clima" desc="Temperatura, condición y pronóstico del destino durante tu viaje." />
          {/* TODO API MENSAJERÍA: conectar con Telegram Bot API + proveedor de email (SendGrid/Resend) */}
          <ApiPlaceholderCard icon={MessageCircle} title="Mensajería" desc="Confirmaciones y recordatorios por Telegram o correo electrónico." />
          {/* TODO API PAGOS: conectar con Stripe/Wompi/Mercado Pago, usar budget.amount como monto */}
          <ApiPlaceholderCard icon={CreditCard} title="Pagos" desc="Pasarela para procesar reservas y pagos de forma segura." />
          {/* TODO API ACTIVIDADES: reemplazar ACTIVITIES estático por fetchActividades() — ver comentario arriba */}
          <ApiPlaceholderCard icon={Compass} title="Actividades en tiempo real" desc="Lugares turísticos, restaurantes y planes disponibles cerca de ti." />
        </div>
      </div>
    </AppShell>
  );
}

// ---------- Root ----------
export default function WiseTrip() {
  const [screen, setScreen] = useState("landing"); // landing | register | login | preferences | dates | budget | review | dashboard
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);

  const [preferences, setPreferences] = useState([]);
  const [dates, setDates] = useState({ start: "", end: "" });
  const [budget, setBudget] = useState({ amount: "", currency: "COP" });

  const duration = useMemo(() => {
    if (!dates.start || !dates.end) return 0;
    return Math.round((new Date(dates.end) - new Date(dates.start)) / (1000 * 60 * 60 * 24));
  }, [dates]);

  const currentUserObj = users.find((u) => u.email === currentUser) || {};

  const resetTripData = () => {
    setPreferences([]);
    setDates({ start: "", end: "" });
    setBudget({ amount: "", currency: "COP" });
  };

  const logout = () => {
    setCurrentUser(null);
    resetTripData();
    setScreen("landing");
  };

  if (screen === "landing") {
    return <LandingScreen goRegister={() => setScreen("register")} goLogin={() => setScreen("login")} />;
  }

  if (screen === "register") {
    return (
      <RegisterScreen
        users={users}
        onRegister={(u) => setUsers((prev) => [...prev, u])}
        goLogin={() => setScreen("login")}
        goLanding={() => setScreen("landing")}
      />
    );
  }

  if (screen === "login") {
    return (
      <LoginScreen
        users={users}
        goRegister={() => setScreen("register")}
        goLanding={() => setScreen("landing")}
        onLogin={(email) => { setCurrentUser(email); setScreen("preferences"); }}
      />
    );
  }

  if (screen === "preferences") {
    return (
      <PreferencesScreen
        selected={preferences}
        setSelected={setPreferences}
        onNext={() => setScreen("dates")}
        user={currentUser}
        onLogout={logout}
      />
    );
  }

  if (screen === "dates") {
    return (
      <DatesScreen
        dates={dates}
        setDates={setDates}
        onNext={() => setScreen("budget")}
        onBack={() => setScreen("preferences")}
        user={currentUser}
        onLogout={logout}
      />
    );
  }

  if (screen === "budget") {
    return (
      <BudgetScreen
        budget={budget}
        setBudget={setBudget}
        onNext={() => setScreen("review")}
        onBack={() => setScreen("dates")}
        user={currentUser}
        onLogout={logout}
      />
    );
  }

  if (screen === "review") {
    return (
      <ReviewScreen
        preferences={preferences}
        dates={dates}
        budget={budget}
        duration={duration}
        onCreate={() => setScreen("dashboard")}
        onBack={() => setScreen("budget")}
        user={currentUser}
        onLogout={logout}
      />
    );
  }

  return (
    <DashboardScreen
      preferences={preferences}
      dates={dates}
      budget={budget}
      duration={duration}
      user={currentUser}
      userName={currentUserObj.fullName}
      onLogout={logout}
      onEdit={() => setScreen("preferences")}
    />
  );
}
