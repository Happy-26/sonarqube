package org.example;


import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
    ExpressionParser parser = new SpelExpressionParser();

    @Test
    void test01() {
        String greetingExp = "Hello, #{ #someWords }";

        context.setVariable("someWords", "world");

        Expression expression = parser.parseExpression(greetingExp, new TemplateParserContext());
        System.out.println(expression.getValue(context, String.class));
    }

    @Test
    void test02() {

        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    @Test
    void test03() {


        // invokes 'getBytes()'
        Expression exp = parser.parseExpression("'Hello World'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        System.out.println(bytes.length);
        for (byte b : bytes) {
            System.out.print(b);
        }
    }

    @Test
    void test04() {


        // invokes 'getBytes().length'
        Expression exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println(length);
    }

    @Test
    void test05() {
        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

        // The constructor arguments are name, birthday, and nationality.
        User zhangsan = new User().setAge(18).setName("zhangsan").setGregorianCalendar(c);


        Expression exp = parser.parseExpression("name"); // Parse name as an expression
        String name = (String) exp.getValue(zhangsan);
        // name == "zhangsan"
        System.out.println(name);
        exp = parser.parseExpression("name == 'zhangsan'");
        boolean result = exp.getValue(zhangsan, Boolean.class);
        // result == true
        System.out.println(result);
    }


    @Test
    void test06() {
        class Simple {
            public List<Boolean> booleanList = new ArrayList<>();
        }


        Simple simple = new Simple();
        simple.booleanList.add(true);


        // "false" is passed in here as a String. SpEL and the conversion service
        // will recognize that it needs to be a Boolean and convert it accordingly.
        parser.parseExpression("booleanList[0]").setValue(context, simple, "false");

        // b is false
        Boolean b = simple.booleanList.get(0);
        System.out.println(b);
    }

    @Test
    void test07() {
        class Demo {
            public List<String> list;
        }

        // Turn on:
        // - auto null reference initialization
        // - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        ExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("list[3]");
        Demo demo = new Demo();
        Object o = expression.getValue(demo);
        // demo.list will now be a real collection of 4 entries
        // Each entry is a new empty String
        System.out.println(o);
    }

    @Test
    void test08() {
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this.getClass().getClassLoader());

        SpelExpressionParser parser = new SpelExpressionParser(config);

        Expression expr = parser.parseExpression("name");

        User message = new User().setName("zhangsan");

        Object payload = expr.getValue(message);
        System.out.println(payload);
    }

    @Test
    void test09() {


        // evaluates to "Hello World"
        String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
        System.out.println(helloWorld);

        // evaluates to "Tony's Pizza"
        String pizzaParlor = (String) parser.parseExpression("'Tony''s Pizza'").getValue();
        System.out.println(pizzaParlor);

        // 6.0221415E23
        double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
        System.out.println(avogadrosNumber);

        // evaluates to 2147483647
        int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        System.out.println(maxValue);

        // true
        boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
        System.out.println(trueValue);

        // null
        Object nullValue = parser.parseExpression("null").getValue();
        System.out.println(nullValue);
    }

    @Test
    void test10() {

        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian").setPlaceOfBirth(new PlaceOfBirth(""));
        System.out.println(tesla.getBirthdate().getYear());

        System.out.println(tesla);
        PlaceOfBirth placeOfBirth = new PlaceOfBirth("New York").setCountry("american");

        parser.parseExpression("placeOfBirth").setValue(context, tesla, placeOfBirth);

        // evaluates to 1856
        int year = (Integer) parser.parseExpression("birthdate.year + 1900").getValue(tesla);
        System.out.println(year);

        String city = (String) parser.parseExpression("placeOfBirth.city").getValue(tesla);
        System.out.println(city);
    }

    @Test
    void test11() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian").setPlaceOfBirth(new PlaceOfBirth(""));
        String[] strings = {"Induction motor", "Electric motor", "Tesla coil", "Tesla battery", "Tesla charger", "Tesla solar panel"};
        Inventor[] inventions = new Inventor[]{new Inventor().setName("John").setInventions(strings), new Inventor().setName("Jane").setInventions(strings), new Inventor().setName("Jack").setInventions(strings), new Inventor().setName("Jill").setInventions(strings), new Inventor().setName("Jessica").setInventions(strings), new Inventor().setName("Joseph").setInventions(strings)};

        Member members = new Member().setMembers(inventions);
        tesla.setInventions(strings);

        // Inventions Array
        // evaluates to "Induction motor"
        String invention = parser.parseExpression("inventions[3]").getValue(context, tesla, String.class);
        System.out.println(invention);

        // Members List
        // evaluates to "Nikola Tesla"
        String name = parser.parseExpression("members[0].name").getValue(context, members, String.class);
        System.out.println(name);

        // List and Array navigation
        // evaluates to "Wireless communication"
        String invention1 = parser.parseExpression("members[0].inventions[4]").getValue(context, members, String.class);
        System.out.println(invention1);
    }

    @Test
    void test12() {
        Map<String, Object> officers = new HashMap<>();
        InventorMap inventorMap = new InventorMap();
        inventorMap.setOfficers(officers);
        officers.put("president", new Inventor().setName("Idvor").setPlaceOfBirth(new PlaceOfBirth("New york", "USA")));
        officers.put("advisors", new Inventor().setName("Joseph").setPlaceOfBirth(new PlaceOfBirth("Jiujins", "USA")));
        officers.put("jlsdffi", new Inventor[]{new Inventor().setPlaceOfBirth(new PlaceOfBirth("", "")), new Inventor().setPlaceOfBirth(new PlaceOfBirth("", ""))});


        // Officer's Dictionary

        Inventor pupin = parser.parseExpression("officers['president']").getValue(context, inventorMap, Inventor.class);
        System.out.println(pupin);

        // evaluates to "Idvor"
        String city = parser.parseExpression("officers['advisors'].placeOfBirth.city").getValue(context, inventorMap, String.class);
        System.out.println(city);
        // setting values
        parser.parseExpression("officers['jlsdffi'][0].placeOfBirth.country").setValue(context, inventorMap, "Croatia");

        System.out.println(((Inventor[]) officers.get("jlsdffi"))[0]);
    }

    @Test
    void test13() {

        // evaluates to a Java list containing the four numbers
        List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);
        numbers.forEach(System.out::println);
        List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);
        listOfLists.forEach(System.out::println);

        // evaluates to a Java map containing the two entries
        Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
        System.out.println(inventorInfo.get("Nikola"));
        Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
        System.out.println(((Map) mapOfMaps.get("name")).get("first"));
    }

    @Test
    void test14() {
        int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);
        for (int i = numbers1.length - 1; i >= 0; i--) {
            System.out.println(numbers1[i]);
        }
        // Array with initializer
        int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);
        for (int i = numbers2.length - 1; i >= 0; i--) {
            System.out.println(numbers2[i]);
        }
        // Multi dimensional array
        int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
        for (int i = numbers3.length - 1; i >= 0; i--) {
            for (int j = numbers3[0].length - 1; j >= 0; j--) {
                System.out.println(numbers3[i][j]);
            }
        }
    }

    @Test
    void test15() {
        Society society = new Society();
        society.setName("society");
        society.getMembers().add(new Inventor().setName("Mihajlo Pupin"));
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);

        // string literal, evaluates to "bc"
        String bc = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);
        System.out.println(bc);
        // evaluates to true
        boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(
                societyContext, Boolean.class);

        System.out.println(isMember);
    }

    @Test
    void test16() {
        // evaluates to true
        boolean a = parser.parseExpression("2 == 2").getValue(Boolean.class);
        System.out.println(a);
        // evaluates to false
        boolean b = parser.parseExpression("2 < -5.0").getValue(Boolean.class);
        System.out.println(b);
        // evaluates to true
        boolean c = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        System.out.println(c);
        // 如果是自定义的对象进行比较。使用的是compareTo方法
    }

    @Test
    void test17() {
        Society society = new Society();
        society.setName("society");
        society.getMembers().add(new Inventor().setName("Mihajlo Pupin"));
        society.getMembers().add(new Inventor().setName("Nikola Tesla"));
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);

        // evaluates to false
        boolean falseValue1 = parser.parseExpression(
                "'xyz' instanceof T(Integer)").getValue(Boolean.class);
        System.out.println(falseValue1);
        // evaluates to true
        boolean trueValue2 = parser.parseExpression(
                "'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        System.out.println(trueValue2);
        // evaluates to false
        boolean falseValue3 = parser.parseExpression(
                "'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        System.out.println(falseValue3);

        boolean trueValue4 = parser.parseExpression(
                "5 instanceof T(Integer)").getValue(Boolean.class);
        System.out.println(trueValue4);

        boolean falseValue5 = parser.parseExpression(
                "5 instanceof T(int)").getValue(Boolean.class);
        System.out.println(falseValue5);

        boolean falseValue6 = parser.parseExpression(
                "5 > 6").getValue(Boolean.class);
        System.out.println(falseValue6);

        boolean falseValue7 = parser.parseExpression(
                "5 gt 6").getValue(Boolean.class);
        System.out.println(falseValue7);

        System.out.println("========================================================");


        // evaluates to true
        String expression1 = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
        boolean trueValue8 = parser.parseExpression(expression1).getValue(societyContext, Boolean.class);
        System.out.println(trueValue8);

        // -- OR --

        // evaluates to true
        boolean trueValue9 = parser.parseExpression("true or false").getValue(Boolean.class);
        System.out.println(trueValue9);

        // evaluates to true
        String expression2 = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
        boolean trueValue10 = parser.parseExpression(expression2).getValue(societyContext, Boolean.class);
        System.out.println(trueValue10);

        // -- NOT --

        // evaluates to false
        boolean falseValue11 = parser.parseExpression("!true").getValue(Boolean.class);
        System.out.println(falseValue11);

        // -- AND and NOT --
        String expression = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
        boolean falseValue12 = parser.parseExpression(expression).getValue(societyContext, Boolean.class);
        System.out.println(falseValue12);

        // evaluates to false
        boolean falseValue13 = parser.parseExpression("true and false").getValue(Boolean.class);
        System.out.println(falseValue13);

        System.out.println("=======================================================================");

        // Addition
        int two = parser.parseExpression("1 + 1").getValue(Integer.class);  // 2
        System.out.println(two);

        String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class);  // 'test string'
        System.out.println(testString);

        // Subtraction
        int four = parser.parseExpression("1 - -3").getValue(Integer.class);  // 4
        System.out.println(four);

        double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class);  // -9000
        System.out.println(d);

        // Multiplication
        int six = parser.parseExpression("-2 * -3").getValue(Integer.class);  // 6
        System.out.println(six);

        double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class);  // 24.0
        System.out.println(twentyFour);

        // Division
        int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class);  // -2
        System.out.println(minusTwo);

        double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class);  // 1.0
        System.out.println(one);

        // Modulus
        int three = parser.parseExpression("7 % 4").getValue(Integer.class);  // 3
        System.out.println(three);

        int one2 = parser.parseExpression("8 / 5 % 2").getValue(Integer.class);  // 1
        System.out.println(one2);

        // Operator precedence
        int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class);  // -21
        System.out.println(minusTwentyOne);

        System.out.println("================================================================");
        Inventor inventor = new Inventor();
        EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();

//        parser.parseExpression("name").setValue(context, inventor, "Aleksandar Seovic");

        // alternatively
        String aleks2 = parser.parseExpression("name = 'Aleksandar Seovic'").getValue(context, inventor, String.class);
        System.out.println(aleks2); // Aleksandar Seovic
    }

    @Test
    void test() {
        // create an array of integers
        List<Integer> primes = new ArrayList<>();
        primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));
        primes.forEach(System.out::println);
        // create parser and set variable 'primes' as the array of integers
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        context.setVariable("primes", primes);
        System.out.println("=======================================");
        // all prime numbers > 10 from the list (using selection ?{...})
        // evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression("#primes.?[#this>10]").getValue(context);
        primesGreaterThanTen.forEach(System.out::println);
    }


    public static String reverseString(String input) {
        StringBuilder backwards = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            backwards.append(input.charAt(input.length() - 1 - i));
        }
        return backwards.toString();
    }

    @Test
    void test18() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();

        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context.setVariable("reverseString",
                AppTest.class.getDeclaredMethod("reverseString", String.class));

        String helloWorldReversed = parser.parseExpression(
                "#reverseString('hello')").getValue(context, String.class);
        System.out.println(helloWorldReversed);
    }

    @Test
    void test19() {
        Society society = new Society();
        society.setName("society");
        society.getMembers().add(new Inventor().setName("Mihajlo Pupin"));
        society.getMembers().add(new Inventor().setName("Nikola Tesla"));
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);

        String falseString = parser.parseExpression(
                "false ? 'trueExp' : 'falseExp'").getValue(String.class);
        System.out.println(falseString); // falseExp


        parser.parseExpression("name").setValue(societyContext, "IEEE");
        societyContext.setVariable("queryName", "Nikola Tesla");

        String expression = "isMember(#queryName)? #queryName + ' is a member of the ' " + "+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";

        String queryResultString = parser.parseExpression(expression)
                .getValue(societyContext, String.class);
        System.out.println(queryResultString);
        // queryResultString = "Nikola Tesla is a member of the IEEE Society"
    }

    @Test
    void test20() {
        ExpressionParser parser = new SpelExpressionParser();

        String name1 = parser.parseExpression("name?:'Unknown'").getValue(new Inventor(), String.class);
        System.out.println(name1);  // 'Unknown'

        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        String name2 = parser.parseExpression("name?:'Elvis Presley'").getValue(context, tesla, String.class);
        System.out.println(name2);  // Nikola Tesla

        tesla.setName(null);
        name2 = parser.parseExpression("name?:'Elvis Presley'").getValue(context, tesla, String.class);
        System.out.println(name2);  // Elvis Presley
    }

    @Test
    void test21() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

        String city = parser.parseExpression("placeOfBirth?.city").getValue(context, tesla, String.class);
        System.out.println(city);  // Smiljan

        tesla.setPlaceOfBirth(null);
        city = parser.parseExpression("placeOfBirth?.city").getValue(context, tesla, String.class);
        System.out.println(city);  // null - does not throw NullPointerException!!!
    }

    @Test
    void test22() {
        Society society = new Society();
        society.setName("society");
        society.getMembers().add(new Inventor().setName("Mihajlo Pupin").setNationality("Serbian"));
        society.getMembers().add(new Inventor().setName("Nikola Tesla"));
        StandardEvaluationContext societyContext = new StandardEvaluationContext(society);


        List<Inventor> list = (List<Inventor>) parser.parseExpression(
                "members.?[nationality == 'Serbian']").getValue(societyContext);

        list.forEach(System.out::println);

        society.getOfficers().put("map1", 1);
        society.getOfficers().put("map2", 2);
        society.getOfficers().put("map3", 22);
        society.getOfficers().put("map4", 222);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext societyContext1 = new StandardEvaluationContext(society);

        Map newMap = (Map) parser.parseExpression("officers.?[value<27]").getValue(societyContext1);
        newMap.forEach((k, v) -> System.out.println(k + " " + v));

    }
}
