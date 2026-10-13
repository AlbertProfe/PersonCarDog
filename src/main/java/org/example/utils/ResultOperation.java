package org.example.utils;

/**
 * MOOC Helsinki - Parte 4 & 5: Concepto de Clase, Atributos Ocultos (Encapsulación)
 * y Métodos con retorno de la propia instancia (Diseño de API fluida / Fluent Setter).
 */
public class ResultOperation {

    // Atributos privados para proteger el estado interno (MOOC Parte 5: Encapsulación)
    private String type;
    private long timestamp;
    private boolean success;
    private String message;
    private Object updatedObject; // MOOC Parte 5: Polimorfismo básico, almacena cualquier entidad o colección

    // Constructor vacío por defecto (MOOC Parte 4: Constructores)
    public ResultOperation() {}

    /**
     * Setter fluido con payload completo (utilizado en Éxitos o Lecturas).
     * Retorna "this" (la propia instancia) para permitir el encadenamiento de llamadas.
     */
    public ResultOperation setter(boolean success, String message, String type, long timestamp, Object updatedObject) {
        this.success = success;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.updatedObject = updatedObject;
        return this; // Permite escribir: return new ResultOperation().setter(...)
    }

    /**
     * Setter fluido simplificado sin payload (utilizado en Fallos o Eliminaciones).
     * Reutiliza el método principal pasando un valor nulo (MOOC Parte 4: Llamadas entre métodos).
     */
    public ResultOperation setter(boolean success, String message, String type, long timestamp) {
        return setter(success, message, type, timestamp, null);
    }

    // ========== GETTERS Y SETTERS ENCAPSULADOS (MOOC Parte 4 & 5) ==========

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getUpdatedObject() { return updatedObject; }
    public void setUpdatedObject(Object updatedObject) { this.updatedObject = updatedObject; }

    // MOOC Parte 4: Personalización del método toString() para la representación textual estructurada del objeto
    @Override
    public String toString() {
        return "resultOperation: {" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", updatedObject=" + updatedObject +
                '}';
    }
}
