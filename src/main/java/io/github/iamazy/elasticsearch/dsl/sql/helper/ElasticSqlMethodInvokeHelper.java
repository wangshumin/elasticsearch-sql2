package io.github.iamazy.elasticsearch.dsl.sql.helper;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.google.common.collect.ImmutableList;
import io.github.iamazy.elasticsearch.dsl.sql.exception.ElasticSql2DslException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author iamazy
 * @date 2019/2/19
 * @descrition
 **/
public class ElasticSqlMethodInvokeHelper {
    static final List<String> DATE_METHOD = ImmutableList.of("date", "to_date", "toDate");


    private static final List<String> AGG_RANGE_METHOD = ImmutableList.of("range", "range_agg");
    private static final List<String> AGG_DATE_HISTOGRAM_METHOD = ImmutableList.of("histogram", "histogram_agg");
    private static final List<String> AGG_RANGE_SEGMENT_METHOD = ImmutableList.of("segment", "segment_agg");

    public static final String AGG_MIN_METHOD = "min";
    public static final String AGG_MAX_METHOD = "max";
    public static final String AGG_AVG_METHOD = "avg";
    public static final String AGG_SUM_METHOD = "sum";

    public static Boolean isMethodOf(List<String> methodAlias, String method) {
        if (CollectionUtils.isEmpty(methodAlias)) {
            return Boolean.FALSE;
        }
        for (String alias : methodAlias) {
            if (alias.equalsIgnoreCase(method)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean isMethodOf(String methodAlias, String method) {
        if (StringUtils.isBlank(methodAlias)) {
            return Boolean.FALSE;
        }
        return methodAlias.equalsIgnoreCase(method);
    }

    public static void checkDateHistogramAggMethod(SQLMethodInvokeExpr aggInvokeExpr) {
        if (!isMethodOf(AGG_DATE_HISTOGRAM_METHOD, aggInvokeExpr.getMethodName())) {
            throw new ElasticSql2DslException("[syntax error] Sql not support method:" + aggInvokeExpr.getMethodName());
        }
    }

    public static void checkRangeAggMethod(SQLMethodInvokeExpr aggInvokeExpr) {
        if (!isMethodOf(AGG_RANGE_METHOD, aggInvokeExpr.getMethodName())) {
            throw new ElasticSql2DslException("[syntax error] Sql not support method:" + aggInvokeExpr.getMethodName());
        }
    }

    public static void checkRangeItemAggMethod(SQLMethodInvokeExpr aggInvokeExpr) {
        if (!isMethodOf(AGG_RANGE_SEGMENT_METHOD, aggInvokeExpr.getMethodName())) {
            throw new ElasticSql2DslException("[syntax error] Sql not support method:" + aggInvokeExpr.getMethodName());
        }
    }

    public static void checkStatAggMethod(SQLAggregateExpr statAggExpr) {
        if (!AGG_MIN_METHOD.equalsIgnoreCase(statAggExpr.getMethodName()) &&
                !AGG_MAX_METHOD.equalsIgnoreCase(statAggExpr.getMethodName()) &&
                !AGG_AVG_METHOD.equalsIgnoreCase(statAggExpr.getMethodName()) &&
                !AGG_SUM_METHOD.equalsIgnoreCase(statAggExpr.getMethodName())) {
            throw new ElasticSql2DslException("[syntax error] Sql not support method:" + statAggExpr.getMethodName());
        }
    }

    public static void checkDateMethod(SQLMethodInvokeExpr dateInvokeExpr) {
        if (!isMethodOf(DATE_METHOD, dateInvokeExpr.getMethodName())) {
            throw new ElasticSql2DslException("[syntax error] Sql not support method:" + dateInvokeExpr.getMethodName());
        }

        if (CollectionUtils.isEmpty(dateInvokeExpr.getParameters()) || dateInvokeExpr.getParameters().size() != 2) {
            throw new ElasticSql2DslException(String.format("[syntax error] There is no %s args method named date",
                    dateInvokeExpr.getParameters() != null ? dateInvokeExpr.getParameters().size() : 0));
        }

        SQLExpr patternArg = dateInvokeExpr.getParameters().get(0);
        SQLExpr timeValArg = dateInvokeExpr.getParameters().get(1);

        if (!(patternArg instanceof SQLCharExpr) && !(patternArg instanceof SQLVariantRefExpr)) {
            throw new ElasticSql2DslException("[syntax error] The first arg of date method should be a time pattern");
        }

        if (!(timeValArg instanceof SQLCharExpr) && !(timeValArg instanceof SQLVariantRefExpr)) {
            throw new ElasticSql2DslException("[syntax error] The second arg of date method should be a string of time");
        }
    }
}










































