package hu.exclusive.dao;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 7315646904027794697L;
    StringBuilder sb = new StringBuilder();

    public ServiceException(Throwable exception) {
        this.prepare(exception);
    }

    public ServiceException(String message) {
        setMessage(message);
    }

    public ServiceException() {
    }

    public void prepare(Throwable exception) {

        // javax.persistence.PersistenceException: org.hibernate.exception.ConstraintViolationException:
        // com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Column 'document_type' cannot be null

        if (exception != null) {

            if (exception instanceof ServiceException) {

                sb.append(exception.getLocalizedMessage());

            } else if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {

                sb.append("::").append("[Adat állapota miatt nem menthető]");
                prepare(((org.springframework.dao.DataIntegrityViolationException) exception).getRootCause());

            } else if (exception instanceof org.hibernate.exception.ConstraintViolationException) {

                sb.append("::").append("[Adat szabálysértés történt]");
                System.err.println("ConstraintViolationException.getSQL() "
                        + ((org.hibernate.exception.ConstraintViolationException) exception).getSQL());

            } else if (exception instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {

                int ercode = ((com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) exception)
                        .getErrorCode();
                sb.append("::").append("[Ütközik ezzel a szabállyal: ").append(exception.getLocalizedMessage())
                        .append("; ERRCODE:" + ercode).append("]");

                prepare(((com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) exception).getCause());

            } else if (exception instanceof javax.persistence.PersistenceException) {

                sb.append("::").append("[Adat mentési hiba]");
                prepare(((javax.persistence.PersistenceException) exception).getCause());

            } else if (exception instanceof org.hibernate.exception.DataException) {

                sb.append("::").append("[Adat hiba]");
                prepare(((org.hibernate.exception.DataException) exception).getCause());

            } else if (exception instanceof com.mysql.jdbc.MysqlDataTruncation) {

                {
                    sb.append("::").append("[Adat méret hiba]");
                }
                prepare(((com.mysql.jdbc.MysqlDataTruncation) exception).getCause());

            } else {

                sb.append("::").append("[").append(exception.getClass().getSimpleName())
                        .append(" hiba történt. A részletek a logba kerültek.").append("]");

                prepare(exception.getCause());
            }
        }
    }

    public void setMessage(String mess) {
        sb.append(mess);
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return sb.length() == 0 ? super.getLocalizedMessage() : sb.toString();
    }

    public static Exception takeReason(Exception e) {
        return new ServiceException(e);
    }
}
