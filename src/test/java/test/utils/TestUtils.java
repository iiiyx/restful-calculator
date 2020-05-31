package test.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsArrayWithSize;

import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

public class TestUtils {
    public static <T> T spyLambda(final T lambda) {
        Class<?>[] interfaces = lambda.getClass().getInterfaces();
        MatcherAssert.assertThat(interfaces, IsArrayWithSize.arrayWithSize(1));
        return mock((Class<T>) interfaces[0], delegatesTo(lambda));
    }
}
