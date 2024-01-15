import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ChineseToEnglishConverter {
    private static Map<String, String> dictionary = new HashMap<>();
    private static Map<String,String> reverseDictionary = new Hashtable<>();
    // 初始化映射表
    static {
        dictionary.put("填空题", "fill");
        dictionary.put("判断题", "judge");
        dictionary.put("单选题", "multiple");
        dictionary.put("多选题", "multiples");
        dictionary.put("主观题", "subjective");
        dictionary.put("语文", "Chinese");
        dictionary.put("数学", "Math");
        dictionary.put("英语", "English");

        // 建立从英文到中文的映射关系
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            reverseDictionary.put(entry.getValue(), entry.getKey());
        }
    }

    // 将中文转换为英文
    public static String convertToEnglish(String chineseText) {
        return dictionary.get(chineseText); // 返回转换后的英文文本
    }
    // 将英文转换为中文
    public static String convertToChinese(String englishText) {
        return reverseDictionary.get(englishText);
    }
   /* public static void main(String[] args) {
        String chineseText = "填空题";
        String englishText = convertToEnglish(chineseText);
        System.out.println(englishText); // 输出: hello world
    }*/
}