package top.zgod.sqlupdatecheck.bean;

import java.sql.JDBCType;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZGOD
 */
public enum JdbcType {
    /*
     * This is added to enable basic support for the
     * ARRAY data type - but a custom type handler is still required
     * 下面注释掉的是已经加了映射处理
     */
    // ARRAY(Types.ARRAY),
    // BIT(Types.BIT),
//    TINYINT(Types.TINYINT),
//    SMALLINT(Types.SMALLINT),
    // INTEGER(Types.INTEGER),
    // BIGINT(Types.BIGINT),
//    FLOAT(Types.FLOAT),
//    REAL(Types.REAL),
//    DOUBLE(Types.DOUBLE),
//    NUMERIC(Types.NUMERIC),
//    DECIMAL(Types.DECIMAL),
//    CHAR(Types.CHAR),
//    VARCHAR(Types.VARCHAR),
//    LONGVARCHAR(Types.LONGVARCHAR),
//    DATE(Types.DATE),
//    TIME(Types.TIME),
//    TIMESTAMP(Types.TIMESTAMP),
//    BINARY(Types.BINARY),
//    VARBINARY(Types.VARBINARY),
//    LONGVARBINARY(Types.LONGVARBINARY),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    //    BLOB(Types.BLOB),
//    CLOB(Types.CLOB),
//    BOOLEAN(Types.BOOLEAN),
// Oracle
    CURSOR(-10),
    UNDEFINED(Integer.MIN_VALUE + 1000),
    // JDK6
//    NVARCHAR(Types.NVARCHAR),
    // JDK6
//    NCHAR(Types.NCHAR),
    // JDK6
//    NCLOB(Types.NCLOB),
    STRUCT(Types.STRUCT),
    JAVA_OBJECT(Types.JAVA_OBJECT),
    DISTINCT(Types.DISTINCT),
    REF(Types.REF),
    DATALINK(Types.DATALINK),
    // JDK6
    ROWID(Types.ROWID),
    // JDK6
//    LONGNVARCHAR(Types.LONGNVARCHAR),
    // JDK6
    SQLXML(Types.SQLXML),
    // SQL Server 2008
    DATETIMEOFFSET(-155),
    // JDBC 4.2 JDK8
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE),
    // JDBC 4.2 JDK8
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE);

    public final int TYPE_CODE;
    private static Map<Integer, JdbcType> codeLookup = new HashMap<>();

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }

    JdbcType(int code) {
        this.TYPE_CODE = code;
        JDBCType[] values = JDBCType.values();
        for (JDBCType value : values) {
            Integer vendorTypeNumber = value.getVendorTypeNumber();
            if (code == vendorTypeNumber) {
                String name = value.getName().toLowerCase();
                System.out.println(name);
            }
        }
    }

    public static JdbcType forCode(int code) {
        return codeLookup.get(code);
    }

}
