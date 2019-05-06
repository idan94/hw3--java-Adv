package Solution;

import Provided.StoryTester;

import java.util.*;
import java.util.stream.Collectors;

public class StoryTesterImpl implements StoryTester {
    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if(testClass==null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        if(testClass==null) {
            throw new IllegalArgumentException();
        }
    }
    public static ArrayList<String> storyToSentenceList(String story){
        String[] array = story.split("\n");
        return Arrays.stream(array).collect(Collectors.toCollection(ArrayList::new));
    }
}
