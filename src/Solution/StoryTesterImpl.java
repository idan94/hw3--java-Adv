package Solution;

import Provided.*;
import org.junit.ComparisonFailure;

import java.awt.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StoryTesterImpl implements StoryTester {
    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        checkStory(story, testClass);
    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        try {
            checkStory(story, testClass);
        } catch (GivenNotFoundException e1)
        {

                for (Class subClass:(testClass.getClasses()))
                {
                    try {
                        //for each sub class, try to run the story REGULARLY until successful
                        //or until all the subclass had run out.
                        testOnNestedClasses(story, subClass);
                    } catch (GivenNotFoundException e2) {continue;}
                    return;
                }
                //if reached here then for all the for iterations, there were
                //exceptions and the function was not found
                //and so we throw the same exception we received
            throw e1;
        }
    }

    public static Method AnnotationsMethod(Class<?> testClass, LegalSentence sentence)
            throws WordNotFoundException {
        if (testClass == null) {
            switch (sentence.getType()) {
                case Given:
                    throw new GivenNotFoundException();
                case When:
                    throw new WhenNotFoundException();
                case Then:
                    throw new ThenNotFoundException();
            }
        }
        ArrayList<Method> toRet = Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> methodIsTypedAs(m, sentence.getType()))
                .collect(Collectors.toCollection(ArrayList::new));
        toRet = toRet.stream()
                .filter(m -> (sentence.getComparable()
                        .equals(AnnotationsToComparable(getAnnoValue(m, sentence.getType())))))
                .collect(Collectors.toCollection(ArrayList::new));
        if (toRet.isEmpty()) {
            return AnnotationsMethod(testClass.getSuperclass(), sentence);
        }
//
        return toRet.get(0);
    }

    public static ArrayList<String> storyToSentenceList(String story) {
        String[] array = story.split("\n");
        return Arrays.stream(array)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //return the value of the annotation
    private static String getAnnoValue(Method func, LegalSentence.Type type) {
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
    private static String AnnotationsToComparable(String str) {
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

    public static boolean methodIsTypedAs(Method func, LegalSentence.Type type) {
        return (type == LegalSentence.Type.Given && func.getAnnotation(Given.class) != null
                || type == LegalSentence.Type.When && func.getAnnotation(When.class) != null
                || type == LegalSentence.Type.Then && func.getAnnotation(Then.class) != null);

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

    private static Object makeBackUp(Object objTest) throws Exception {
        Object backUp = objTest.getClass().getConstructor().newInstance();
        for (Field fFrom : objTest.getClass().getFields()) {
            Object fieldTemp = new Object();
            if (fFrom.get(objTest) instanceof Cloneable) {//Clone:
                for (Class<?> c = fFrom.getClass();
                     c == null;
                     c = c.getSuperclass()) {
                    try {
                        fieldTemp = c.getDeclaredMethod("clone").invoke(fFrom.get(objTest));
                    } catch (NoSuchMethodException e) {
                    } catch (NullPointerException e) {
                        throw e;
                    }

                }
            } else {//Copy constructor:
                try {
                    fieldTemp = fFrom.getClass().getDeclaredConstructor(fFrom.getType()).newInstance(fFrom.get(objTest));
                }catch (NoSuchMethodException e) {//just save the object
                    fieldTemp = fFrom.get(objTest);
                }
            }
            backUp.getClass().getField(fFrom.getName()).set(backUp, fieldTemp);
        }
        return backUp;
    }
    /**
     * main function, gets a story and test class,
     * and run the matching methods to the annotations in the story
     *
     * @param story     the story given, as String.
     * @param testClass the test class given from user, includes all the methods with annotations
     * @throws Exception
     */
    private static void checkStory(String story, Class<?> testClass) throws Exception {
        Object objTest = testClass.getConstructor().newInstance();
        int thenFailedCounter = 0;
        String firstThenFailed = "";
        ArrayList<String> firstThenFailedExpected = new ArrayList<>();
        ArrayList<String> firstThenFailedActual = new ArrayList<>();

        for (String sentence : storyToSentenceList(story)) {
            //for EACH sentence do:
            LegalSentence tempLegalSentence = new LegalSentence(sentence);
            Method tempMethod = AnnotationsMethod(testClass, tempLegalSentence);
            ArrayList<ArrayList<String>> parameters = tempLegalSentence.getParameters();
            boolean methodThenSuccessFlag = false;//used for Then sentence with "or"'s
            for (ArrayList<String> layerOfParameters : parameters) {
                try {
                    tempMethod.invoke(objTest, fixParameters(layerOfParameters, tempMethod));
                    methodThenSuccessFlag = true;
                    break;
                    //Given/When run only one time- fine
                    //for Then- if one of them was successful, we want to stop check.
                } catch (ComparisonFailure e) {
                    assert (tempLegalSentence.getType() == LegalSentence.Type.Then);
                    firstThenFailedExpected.add(e.getExpected());
                    firstThenFailedActual.add(e.getActual());
                    //empty because if we get in here, the flag is still on FALSE
                }
            }
            if (!methodThenSuccessFlag) { // if flag is false- means 'Then' FAILED
                assert (tempLegalSentence.getType() == LegalSentence.Type.Then);
                if (thenFailedCounter == 0) {
                    firstThenFailed = tempLegalSentence.getInput();
                }
                thenFailedCounter++;

            }


        }
        if (thenFailedCounter > 0) {
            throw new StoryTestExceptionImpl(firstThenFailed, firstThenFailedExpected,
                    firstThenFailedActual, thenFailedCounter);
        }
    }
}

