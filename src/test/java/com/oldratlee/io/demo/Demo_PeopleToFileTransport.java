package com.oldratlee.io.demo;

import com.oldratlee.io.core.Input;
import com.oldratlee.io.core.Output;
import com.oldratlee.io.core.Receiver;
import com.oldratlee.io.core.Sender;
import com.oldratlee.io.core.filter.Transforms;
import com.oldratlee.io.core.filter.Function;
import com.oldratlee.io.utils.Outputs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo_PeopleToFileTransport {
    static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }


    static class PeopleInput implements Input<Person, IOException> {

        List<Person> people;

        PeopleInput(List<Person> people) {
            this.people = people;
        }


        @Override
        public <ReceiverThrowableType extends Throwable> void transferTo(Output<Person, ReceiverThrowableType> output) throws IOException, ReceiverThrowableType {
            final PeopleSender sender = new PeopleSender(people);
            output.receiveFrom(sender);
        }

    }


    static class PeopleSender implements Sender<Person, IOException> {
        List<Person> people;

        public PeopleSender(List<Person> people) {
            this.people = people;
        }

        @Override
        public <ReceiverThrowableType extends Throwable> void sendTo(Receiver<Person, ReceiverThrowableType> receiver) throws ReceiverThrowableType, IOException {
            for (Person person : this.people) {
                receiver.receive(person);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Function<Person, String> function = new Function<Person, String>() {
            private final String LINE_SEPARATOR = System.getProperty("line.separator");

            public String map(Person from) {
                StringBuffer r = new StringBuffer();
                r.append("name: " + from.name + ", ").append("age: " + from.age + ";" + LINE_SEPARATOR);
                return r.toString();
            }
        };
        List<Person> people = new ArrayList<Person>();
        people.add(new Person("oldratlee", 30));
        people.add(new Person("yangkai", 33));
        people.add(new Person("huoxinping", 34));

        File destination = new File("out.tmp");
        PeopleInput input = new PeopleInput(people);
        Output<String, IOException> output = Outputs.text(destination);
        input.transferTo(Transforms.map(function, output));
    }

}
