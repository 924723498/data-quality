// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.datamasking.functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.talend.dataquality.duplicating.RandomWrapper;

/**
 * created by jgonzalez on 24 juin 2015. This function works like GenerateFromFile, the only difference is that it will
 * use the hashCode() function provided by Java to choose an element from the list. When having the hashCode, we apply a
 * modulo according to the number of elements in the list.
 *
 */
public abstract class GenerateFromFileHash<T> extends Function<T> {

    private static final long serialVersionUID = -4616169672287269594L;

    protected List<String> StringTokens = new ArrayList<>();

    protected List<T> genericTokens = new ArrayList<T>();

    protected void init() {
        try {
            StringTokens = KeysLoader.loadKeys(parameters[0]);
        } catch (IOException | NullPointerException e) {
            // We do nothing here because in is already set.
        }
    }

    @Override
    public void parse(String extraParameter, boolean keepNullValues, RandomWrapper rand) {
        super.parse(extraParameter, keepNullValues, rand);
        this.init();
    }

    @Override
    protected T doGenerateMaskedField(T t) {
        if (genericTokens.size() > 0) {
            if (t == null) {
                return genericTokens.get(rnd.nextInt(genericTokens.size()));
            } else {
                return genericTokens.get(Math.abs(t.hashCode() % genericTokens.size()));
            }
        } else {
            return getDefaultOutput();
        }
    }

    protected abstract T getDefaultOutput();

}
