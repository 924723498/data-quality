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
package org.talend.dataquality.semantic.classifier.custom;

import java.util.HashSet;
import java.util.Set;

import org.talend.dataquality.semantic.classifier.ISubCategory;
import org.talend.dataquality.semantic.classifier.SemanticCategoryEnum;
import org.talend.dataquality.semantic.classifier.impl.AbstractSubCategoryClassifier;
import org.talend.dataquality.semantic.filter.ISemanticFilter;
import org.talend.dataquality.semantic.recognizer.MainCategory;
import org.talend.dataquality.semantic.validator.ISemanticValidator;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class UserDefinedClassifier extends AbstractSubCategoryClassifier {

    /**
     * Method "addSubCategory" adds a subcategory if it does not already exists.
     * 
     * @param category the category to add
     * @return true when the category is added
     */
    public boolean addSubCategory(UserDefinedCategory category) {
        return potentialSubCategories.add(category);
    }

    /**
     * Method "insertOrUpdateSubCategory" inserts or update a category.
     * 
     * @param category the category to insert or update
     * @return true when the category is correctly either inserted or updated
     */
    public boolean insertOrUpdateSubCategory(UserDefinedCategory category) {
        if (potentialSubCategories.contains(category)) {
            return updateSubCategory(category);
        } // else
        return addSubCategory(category);
    }

    /**
     * Method "updateSubCategory" updates a given category.
     * 
     * @param category the category to update
     * @return true if the category exists and is updated.
     */
    public boolean updateSubCategory(UserDefinedCategory category) {
        if (removeSubCategory(category)) {
            return addSubCategory(category);
        }
        return false;
    }

    public boolean removeSubCategory(UserDefinedCategory category) {
        return potentialSubCategories.remove(category);
    }

    @Override
    public Set<ISubCategory> classifyIntoCategories(String str) {
        MainCategory mainCategory = MainCategory.getMainCategory(str);
        return classifyIntoCategories(str, mainCategory);
    }

    public Set<ISubCategory> classifyIntoCategories(String str, MainCategory mainCategory) {
        Set<ISubCategory> catSet = new HashSet<>();
        if (mainCategory == MainCategory.UNKNOWN || mainCategory == MainCategory.NULL || mainCategory == MainCategory.BLANK) {
            // FIXME return UNKNOWN instead of EMPTY as the category ID, require synchronization with dataprep team
            catSet.add(new UserDefinedCategory("", SemanticCategoryEnum.UNKNOWN));
            return catSet;
        }

        // else
        for (ISubCategory classifier : potentialSubCategories) {
            // if the MainCategory is different, ignor it and continue;AlphaNumeric rule should contain pure Alpha and
            // Numeric.
            UserDefinedCategory userDefCat = (UserDefinedCategory) classifier;
            MainCategory classifierCategory = userDefCat.getMainCategory();

            if (mainCategory == MainCategory.Alpha) {
                if (classifierCategory != MainCategory.Alpha && classifierCategory != MainCategory.AlphaNumeric) {
                    continue;
                }
            } else if (mainCategory == MainCategory.Numeric) {
                if (classifierCategory != MainCategory.Numeric && classifierCategory != MainCategory.AlphaNumeric) {
                    continue;
                }
            } else if (classifierCategory != mainCategory) {
                continue;
            }

            ISemanticFilter filter = classifier.getFilter();

            if (filter != null) {
                if (!filter.isQualified(str)) {
                    continue;
                }
            }
            ISemanticValidator validator = classifier.getValidator();
            if (validator != null && validator.isValid(str)) {
                catSet.add(classifier);
            }
        }

        return catSet;

    }

    /**
     * @deprecated use {@link #classifyIntoCategories(String)} instead
     * 
     * @see org.talend.dataquality.semantic.classifier.impl.AbstractSubCategoryClassifier#classify(java.lang.String)
     */
    @Deprecated
    @Override
    public Set<String> classify(String str) {
        MainCategory mainCategory = MainCategory.getMainCategory(str);
        return classify(str, mainCategory);
    }

    /**
     * @deprecated use {@link #classifyIntoCategories(String, MainCategory)} instead Validate this input data to adpt
     * which customed rules.
     * <p/>
     * Actually, the main category can be calculated based on the input string, but this method has better performance
     * in case the mainCategory is already calculated previously.
     * 
     * @param str is input data
     * @param mainCategory: the MainCategory is computed by the input data
     * @return
     */
    @Deprecated
    public Set<String> classify(String str, MainCategory mainCategory) {
        Set<ISubCategory> categories = classifyIntoCategories(str, mainCategory);
        Set<String> catSet = new HashSet<>();
        for (ISubCategory iSubCategory : categories) {
            catSet.add(iSubCategory.getId());
        }

        return catSet;
    }
}
