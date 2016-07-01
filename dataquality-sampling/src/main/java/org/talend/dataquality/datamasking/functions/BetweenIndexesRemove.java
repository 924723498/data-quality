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

/**
 * created by jgonzalez on 22 juin 2015. This class is used when the requested function is BetweenIndexesKeep. It will
 * return a new String that will contain all the input elements but those which are between the bounds given as
 * parameter.
 *
 */
public class BetweenIndexesRemove extends BetweenIndexes {

    private static final long serialVersionUID = -198474878692287672L;

    @Override
    protected void initAttributes() {
        beginIndex = Integer.valueOf(parameters[0]) - 1;
        endIndex = Integer.valueOf(parameters[1]);
        toRemove = true;

    }

    @Override
    protected boolean validParameters() {
        return parameters.length == 2 && patternNumber.matcher(parameters[0]).matches()
                && patternNumber.matcher(parameters[1]).matches();
    }
}
