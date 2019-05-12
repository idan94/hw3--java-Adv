package tests;


import Solution.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static Solution.LegalSentence.*;

public class test {

    public void nadavTest() {
        String str = "hello world\nopp frikin sucks!";
        String[] array = str.split(" |\n");
        for (String word : array) {
            System.out.println(word);
        }
    }

    @Test
    public void IdanTest() throws Exception{
        Class<?> theClass = DogStoryTest.class;
        Type type = Type.Given;

        ArrayList<String> func = Arrays.stream(theClass.getDeclaredMethods()).filter(m -> m.getAnnotation(Given.class) != null)
                .map(m-> getAnnoValue(m,type))
                .collect(Collectors.toCollection(ArrayList::new));

        String str = "Given a Dog of age 6\n"
                + "When the dog is not taken out for a walk, the number of hours is 5\n"
                + "Then the house condition is clean";
        Story story = new Story(str);

        boolean a = StoryTesterImpl.methodIsTypedAs(theClass.getDeclaredMethods()[0],Type.Given);
        String name = StoryTesterImpl.AnnotationsMethod(theClass,story.getSentence().get(0)).getName();
        System.out.println();

    }
    private String getAnnoValue (Method func, Type type)
    {
        switch (type)
        {
            case When: return  func.getAnnotation(When.class).value();
            case Given: return  func.getAnnotation(Given.class).value();
            case Then: return func.getAnnotation(Then.class).value();
        }
        return null;
    }
    private String AnnotaionsToComapreable (String str)
    {
        String[] array = str.split(" ");
        StringBuilder toRet= new StringBuilder();
        for(int i = 0 ;i < array.length ;i++)
        {
            if(array[i].charAt(0) == '&')
            {
                array[i] = "&";
            }
            toRet.append("  ").append(array[i]);
        }
        return toRet.toString();
    }
}

