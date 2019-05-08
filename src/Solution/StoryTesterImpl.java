package Solution;

import Provided.StoryTester;

import java.awt.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
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
        if(testClass==null) {
            throw new IllegalArgumentException();
        }
    }
    public static Method AnnotaionsMethod(Class<?> testClass, LeagalSentnce cent)
    {
        ArrayList<Method> toRet =  Arrays.stream(testClass.getDeclaredMethods()).filter(m->methodIsTypedAs(m,cent.getType()))
                .collect(Collectors.toCollection(ArrayList::new));
        String s1 = cent.getComperable();
        String s2 = AnnotaionsToComapreable(getAnnoVlaue(toRet.get(0),cent.getType()));
        toRet = toRet.stream().filter(m->(cent.getComperable().equals(AnnotaionsToComapreable(getAnnoVlaue(m,cent.getType())))))
                .collect(Collectors.toCollection(ArrayList::new));
        if(toRet.isEmpty())
        {
            return null;
        }
        return toRet.get(0);
    }
    public static ArrayList<String> storyToSentenceList(String story){
        String[] array = story.split("\n");
        return Arrays.stream(array).collect(Collectors.toCollection(ArrayList::new));
    }
    //return the value of the annotation
    private static String getAnnoVlaue(Method func, LeagalSentnce.Type type)
    {
        switch (type)
        {
            case Given: return  func.getAnnotation(Given.class).value();
            case When: return  func.getAnnotation(When.class).value();
            default: return func.getAnnotation(Then.class).value();
        }
    }
    //gets an annotation (not an instance with parameters) and returns the comparable version of it
    private static String AnnotaionsToComapreable(String str)
    {
        String[] array = str.split(" ");
        StringBuilder toRet= new StringBuilder();
        for(int i = 0 ;i < array.length ;i++)
        {
            if(array[i].charAt(0) == '&')
            {
                array[i] = "&";
            }
            toRet.append(" ").append(array[i]);
        }
        return toRet.toString().split(" ",2)[1];
    }
    public static boolean methodIsTypedAs(Method func, LeagalSentnce.Type type)
    {
        if(type == LeagalSentnce.Type.Given && func.getAnnotation(Given.class)!=null)
        {
            return true;
        }
        else if(type == LeagalSentnce.Type.When && func.getAnnotation(When.class)!=null)
        {
            return true;
        }
        else if(type == LeagalSentnce.Type.Then && func.getAnnotation(Then.class)!=null)
        {
            return true;
        }
        return false;
    }
}
