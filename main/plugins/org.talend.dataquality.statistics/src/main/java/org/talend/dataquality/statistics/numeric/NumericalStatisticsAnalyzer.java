// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.statistics.numeric;

import java.util.ArrayList;
import java.util.List;

import org.talend.datascience.common.inference.AbstractAnalyzer;
import org.talend.datascience.common.inference.type.DataType;
import org.talend.datascience.common.inference.type.DataType.Type;

/**
 * 
 * Analyzer interface of numerical data.
 * 
 * @author zhao
 *
 * @param <T>
 */
public abstract class NumericalStatisticsAnalyzer<T> extends AbstractAnalyzer<T> {

    private static final long serialVersionUID = 5444082810385180383L;

    private Integer[] statColIdx; // index arrays indicator which column are numerical that need to be analyzed.

    private DataType.Type[] types; // types of columns

    /**
     * 
     * @param types the array of columns' type
     */
    public NumericalStatisticsAnalyzer(Type[] types) {
        if (types == null || types.length == 0) {
            throw new IllegalArgumentException("types are required to compute numerical statistics.");
        }
        setTypes(types);
    }

    @Override
    public void init() {
        setStatColIdx();
    }

    /**
     * 
     * @return the array of columns' type
     */
    public DataType.Type[] getTypes() {
        return types;
    }

    /**
     * 
     * @param types the array of columns' type note: statColIdx will be synchronize with the new array of types
     */
    private void setTypes(DataType.Type[] types) {
        this.types = types;
        if (types == null)
            this.statColIdx = null;
        else
            setStatColIdx();
    }

    /**
     * 
     * @return index arrays indicator which column are numerical that need to be analyzed
     */
    public Integer[] getStatColIdx() {
        return statColIdx;
    }

    private void setStatColIdx() {
        List<Integer> statIdxList = new ArrayList<Integer>();
        for (int idCol = 0; idCol < types.length; idCol++) {
            if (types[idCol].equals(Type.INTEGER) || types[idCol].equals(Type.DOUBLE)) {
                statIdxList.add(idCol);
            }
        }
        this.statColIdx = statIdxList.toArray(new Integer[statIdxList.size()]);
    }

    @Override
    public void close() throws Exception {

    }

}
