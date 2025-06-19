package coboxlogistic.startup.backend.shared.infrastructure.persistence.jpa.configuration.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

/**
 * Custom physical naming strategy for the Cobox Backend Platform.
 * Implements snake_case naming with pluralized table names following DDD conventions.
 *
 * @author Cobox Team
 * @version 1.0
 */
public class SnakeCaseWithPluralizedTablePhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }

        String tableName = name.getText();
        if (tableName == null || tableName.isEmpty()) {
            return name;
        }

        // Convert to snake_case
        String snakeCaseName = toSnakeCase(tableName);

        // Pluralize table names (simple implementation)
        String pluralizedName = pluralize(snakeCaseName);

        return Identifier.toIdentifier(pluralizedName, name.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }

        String columnName = name.getText();
        if (columnName == null || columnName.isEmpty()) {
            return name;
        }

        String snakeCaseName = toSnakeCase(columnName);
        return Identifier.toIdentifier(snakeCaseName, name.isQuoted());
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }

        String sequenceName = name.getText();
        if (sequenceName == null || sequenceName.isEmpty()) {
            return name;
        }

        String snakeCaseName = toSnakeCase(sequenceName);
        return Identifier.toIdentifier(snakeCaseName, name.isQuoted());
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }

        String catalogName = name.getText();
        if (catalogName == null || catalogName.isEmpty()) {
            return name;
        }

        String snakeCaseName = toSnakeCase(catalogName);
        return Identifier.toIdentifier(snakeCaseName, name.isQuoted());
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }

        String schemaName = name.getText();
        if (schemaName == null || schemaName.isEmpty()) {
            return name;
        }

        String snakeCaseName = toSnakeCase(schemaName);
        return Identifier.toIdentifier(snakeCaseName, name.isQuoted());
    }

    /**
     * Convert camelCase to snake_case
     * @param camelCase the camelCase string
     * @return snake_case string
     */
    private String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));

        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    /**
     * Simple pluralization logic for table names
     * @param singular the singular form
     * @return the plural form
     */
    private String pluralize(String singular) {
        if (singular == null || singular.isEmpty()) {
            return singular;
        }

        String lowerSingular = singular.toLowerCase(Locale.ROOT);

        // Common pluralization rules
        if (lowerSingular.endsWith("y") && !lowerSingular.endsWith("ay") && !lowerSingular.endsWith("ey") && !lowerSingular.endsWith("iy") && !lowerSingular.endsWith("oy") && !lowerSingular.endsWith("uy")) {
            return singular.substring(0, singular.length() - 1) + "ies";
        } else if (lowerSingular.endsWith("s") || lowerSingular.endsWith("sh") || lowerSingular.endsWith("ch") || lowerSingular.endsWith("x") || lowerSingular.endsWith("z")) {
            return singular + "es";
        } else if (lowerSingular.endsWith("f") || lowerSingular.endsWith("fe")) {
            if (lowerSingular.endsWith("fe")) {
                return singular.substring(0, singular.length() - 2) + "ves";
            } else {
                return singular.substring(0, singular.length() - 1) + "ves";
            }
        } else {
            return singular + "s";
        }
    }
}