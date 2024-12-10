package project.utility;

import com.mifmif.common.regex.Generex;

import static project.utility.CommonUtils.*;

public class CodeUtils {

    private static final Generex sortCodeRegex = new Generex(SORT_CODE_REGEX_STRING);
    private static final Generex accountNumberRegex = new Generex(ACCOUNT_NUMBER_REGEX_STRING);

    public CodeUtils() {}

    public static String getNewSortCode() {
        return sortCodeRegex.random();
    }

    public static String getNewAccountNumber() {
        return accountNumberRegex.random();
    }
}
