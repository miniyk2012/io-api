package com.oldratlee.io.demo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.oldratlee.io.core.Input;
import com.oldratlee.io.core.Output;
import com.oldratlee.io.core.filter.Transforms;
import com.oldratlee.io.core.filter.Function;
import com.oldratlee.io.utils.Inputs;
import com.oldratlee.io.utils.Outputs;

/**
 * @author oldratlee
 */
public class Demo_Intercept_CountLine {
    public static void main(String[] args) throws IOException {
        File source = new File("in.txt");
        File destination = new File("out.tmp");
        final AtomicInteger count = new AtomicInteger();

        Input<String, IOException> input = Inputs.text(source);
        
        Output<String, IOException> output = Outputs.text(destination);
        
        Function<String, String> function = new Function<String, String>() {
            public String map(String from) {
                count.incrementAndGet();
                return from;
            }
        };
        
        input.transferTo(Transforms.map(function, output));
        
        System.out.println("Counter: " + count.get());
    }
}
