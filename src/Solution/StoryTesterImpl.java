package Solution;

import Provided.StoryTester;
import org.junit.ComparisonFailure;

import java.awt.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StoryTesterImpl implements StoryTester {
    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {

    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        if (testClass == null) {
            throw new IllegalArgumentException();
        }
    }

    public static Method AnnotationsMethod(Class<?> testClass, LegalSentnce sentence) {
        ArrayList<Method> toRet = Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> methodIsTypedAs(m, sentence.getType()))
                .collect(Collectors.toCollection(ArrayList::new));
        String s1 = sentence.getComparable();
        String s2 = AnnotationsToComaparable(getAnnoValue(toRet.get(0), sentence.getType()));
        toRet = toRet.stream()
                .filter(m -> (sentence.getComparable()
                        .equals(AnnotationsToComaparable(getAnnoValue(m, sentence.getType())))))
                .collect(Collectors.toCollection(ArrayList::new));
        if (toRet.isEmpty()) {
            return null;
        }
        return toRet.get(0);
    }

    public static ArrayList<String> storyToSentenceList(String story) {
        String[] array = story.split("\n");
        return Arrays.stream(array)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //return the value of the annotation
    private static String getAnnoValue(Method func, LegalSentnce.Type type) {
        switch (type) {
            case Given:
                return func.getAnnotation(Given.class).value();
            case When:
                return func.getAnnotation(When.class).value();
            case Then:
                return func.getAnnotation(Then.class).value();
            default:
                return "Error";//no need default; TODO: maybe to throw exception here
        }
    }

    //gets an annotation (not an instance with parameters)
    //returns the comparable version of it
    private static String AnnotationsToComaparable(String str) {
        String[] array = str.split(" ");
        StringBuilder toRet = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i].charAt(0) == '&') {
                array[i] = "&";
            }
            toRet.append(" ").append(array[i]);
        }
        return toRet.toString().split(" ", 2)[1];
    }

    public static boolean methodIsTypedAs(Method func, LegalSentnce.Type type) {
        return (type == LegalSentnce.Type.Given && func.getAnnotation(Given.class) != null
                || type == LegalSentnce.Type.When && func.getAnnotation(When.class) != null
                || type == LegalSentnce.Type.Then && func.getAnnotation(Then.class) != null);

    }

    /**
     * fix strings to integer if needed
     *
     * @param parameters the current layer of parameters
     * @param method     the method we want to invoke with the given parameters
     * @return array of OBJECT(for having INTEGER+STRING both)
     */
    private static Object[] fixParameters(ArrayList<String> parameters,
                                          Method method) {
        int i = 0;//index of argument in parameters(the given sentence)
        Object[] toRet = new Object[method.getParameterTypes().length];
        for (Class<?> typeOfParameter : method.getParameterTypes()) {
            if (typeOfParameter == Integer.class) {
                toRet[i] = Integer.parseInt(parameters.get(i));
            } else {
                toRet[i] = parameters.get(i);
                i++;
            }
        }
        return toRet;
    }

    //Function for the boss: Nadav
    private static void forNadav(String story, Class<?> testClass) throws Exception {
        try {
            Object objTest = testClass.getConstructor().newInstance();
            for (String sentence : storyToSentenceList(story)) {
                //for EACH sentence do:
                LegalSentnce tempLegalSentence = new LegalSentnce(sentence);
                Method tempMethod = AnnotationsMethod(testClass, tempLegalSentence);
                ArrayList<ArrayList<String>> parameters = tempLegalSentence.getParameters();
                if (tempMethod == null || parameters == null) {
                    throw new Exception(); //TODO: Edit! throw exception
                } else {
                    boolean methodThenSuccessFlag = false;//used for Then sentence with "or"'s
                    for (ArrayList<String> layerOfParameters : parameters) {
                        try {
                            tempMethod.invoke(objTest, fixParameters(layerOfParameters, tempMethod));
                            methodThenSuccessFlag = true;
                        } catch (ComparisonFailure e) {
                            //empty because if we get in here, the flag is still on FALSE
                        }
                        if (methodThenSuccessFlag) {
                            break;
                        }
                    }
                }
            }


            //TODO: handed all catches, check what to do or throw
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
