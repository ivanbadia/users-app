package infrastructure.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class RoutePath {

    //path params are defined between {}
    private static final Pattern EVERYTHING_BETWEEN_BRACKETS = Pattern.compile("\\{(.*?)\\}");
    private final static String EVERYTHING_UNTIL_SLASH = "([^/]*)";


    private final List<String> paramNames;
    private final Pattern pathRegex;


    public RoutePath(String path) {
        pathRegex = Pattern.compile(routeRegex(path));
        paramNames = paramNamesOf(path);
    }

    private static List<String> paramNamesOf(String path) {
        Matcher m = EVERYTHING_BETWEEN_BRACKETS.matcher(path);
        List<String> matches = new ArrayList<>();
        while (m.find()){
            matches.add(m.group(1));
        }

        return matches;
    }

    public boolean matches(String path) {
        Matcher matcher = pathRegex.matcher(path);
        return matcher.matches();
    }

    public Map<String, String> pathParamsOf(String path){
        Map<String, String> pathParameters = new HashMap<>();
        List<String> pathParamValues = paramValuesOf(path);
        for(int i = 0; i < paramNames.size();i++){
            pathParameters.put(paramNames.get(i), pathParamValues.get(i));
        }
        return pathParameters;
    }


    private static String routeRegex(String path) {

        String newPath = path.replaceAll("\\*", "(.*)");
        Matcher matcher = EVERYTHING_BETWEEN_BRACKETS.matcher(newPath);

        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, EVERYTHING_UNTIL_SLASH);
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();


    }


    private List<String> paramValuesOf(String path) {
        List<String> values = new ArrayList<>();

        Matcher m = pathRegex.matcher(path);

        if (m.matches()) {
            for (int i = 0; i < m.groupCount(); i++) {
                values.add(m.group(i + 1));
            }
        }

        return values;
    }
}
