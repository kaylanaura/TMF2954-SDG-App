// Class      : LearningData
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : [Member 2 Name]
// Description: Provides all LearningContent data for the 10 SDG 3 topics.
//              Each page now includes an imagePath pointing to assets/final_images/.

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LearningData {

    private static final String IMG = "assets/final_images/";

    public static Map<String, List<LearningContent>> getAllTopics() {
        Map<String, List<LearningContent>> topics = new LinkedHashMap<>();

        // ── Topic 1: What is SDG 3? ───────────────────────────────────────────
        List<LearningContent> t1 = new ArrayList<>();
        t1.add(new LearningContent(
            "SDG 3 — Good Health & Well-Being",
            "SDG 3 is one of the 17 Sustainable Development Goals set by the\n" +
            "United Nations. Its mission is to ensure healthy lives and promote\n" +
            "well-being for all people of all ages by the year 2030.\n\n" +
            "It covers 13 targets ranging from reducing maternal and child\n" +
            "mortality to achieving universal health coverage and addressing\n" +
            "mental health.",
            "Key target",
            "Reduce the global maternal mortality ratio to less than\n70 per 100,000 live births by 2030.",
            "heart", "#1D9E75", 1, "What is SDG 3?",
            IMG + "01_sdg3_intro.png"
        ));
        topics.put("What is SDG 3?", t1);

        // ── Topic 2: Mental Health (2 pages) ──────────────────────────────────
        List<LearningContent> t2 = new ArrayList<>();
        t2.add(new LearningContent(
            "Understanding Mental Health",
            "Mental health includes our emotional, psychological, and social\n" +
            "well-being. It affects how we think, feel, and act every day.\n\n" +
            "1 in 4 people globally will experience a mental health issue at\n" +
            "some point in their lives. Depression, anxiety, and stress are\n" +
            "among the most common conditions.",
            "Did you know?",
            "Depression is the leading cause of disability worldwide,\naffecting over 280 million people.",
            "brain", "#7F77DD", 1, "Mental Health",
            IMG + "02_mental_health.png"
        ));
        t2.add(new LearningContent(
            "Managing Stress & Anxiety",
            "Common strategies for managing mental health include:\n" +
            "  • Regular exercise (releases mood-boosting endorphins)\n" +
            "  • Adequate sleep (7-9 hours per night)\n" +
            "  • Talking to someone you trust\n" +
            "  • Mindfulness and deep breathing\n" +
            "  • Limiting social media use\n\n" +
            "Seeking professional help is a sign of strength, not weakness.",
            "Tip",
            "Try the 5-4-3-2-1 grounding technique: name 5 things\nyou see, 4 you feel, 3 you hear, 2 you smell, 1 you taste.",
            "brain", "#7F77DD", 2, "Mental Health",
            IMG + "03_stress_tips.png"
        ));
        topics.put("Mental Health", t2);

        // ── Topic 3: Physical Activity ────────────────────────────────────────
        List<LearningContent> t3 = new ArrayList<>();
        t3.add(new LearningContent(
            "Why Exercise Matters",
            "The WHO recommends at least 150 minutes of moderate-intensity\n" +
            "physical activity per week for adults.\n\n" +
            "Regular exercise reduces the risk of:\n" +
            "  • Heart disease and stroke\n" +
            "  • Type 2 diabetes\n" +
            "  • Depression and anxiety\n" +
            "  • Certain types of cancer\n\n" +
            "It also improves sleep quality, energy levels, and mood.",
            "WHO Guideline",
            "Children aged 5-17 should get at least 60 minutes of\nmoderate to vigorous physical activity daily.",
            "run", "#BA7517", 1, "Physical Activity",
            IMG + "04_physical_activity.png"
        ));
        topics.put("Physical Activity", t3);

        // ── Topic 4: Diseases (2 pages) ───────────────────────────────────────
        List<LearningContent> t4 = new ArrayList<>();
        t4.add(new LearningContent(
            "Communicable Diseases",
            "Communicable diseases spread from person to person or via vectors\n" +
            "like mosquitoes. Key examples include:\n" +
            "  • HIV/AIDS: transmitted through blood, sexual contact\n" +
            "  • Tuberculosis (TB): airborne bacterial infection\n" +
            "  • Malaria: spread by infected mosquitoes\n\n" +
            "These diseases disproportionately affect low-income countries\nwith limited access to healthcare.",
            "Global Fact",
            "In 2022, tuberculosis caused 1.3 million deaths, making\nit one of the world's top infectious disease killers.",
            "virus", "#D85A30", 1, "Diseases",
            IMG + "05_communicable.png"
        ));
        t4.add(new LearningContent(
            "Non-Communicable Diseases (NCDs)",
            "NCDs are not passed between people. They include:\n" +
            "  • Heart disease (leading cause of death globally)\n" +
            "  • Cancer (second leading cause)\n" +
            "  • Diabetes (affects 422 million people worldwide)\n" +
            "  • Chronic respiratory diseases\n\n" +
            "NCDs account for 63% of all global deaths and are largely\npreventable through healthy lifestyles.",
            "Prevention Tip",
            "Avoiding tobacco, eating well, staying active, and limiting\nalcohol reduces NCD risk significantly.",
            "stethoscope", "#D85A30", 2, "Diseases",
            IMG + "06_ncd.png"
        ));
        topics.put("Diseases", t4);

        // ── Topic 5: Vaccines ─────────────────────────────────────────────────
        List<LearningContent> t5 = new ArrayList<>();
        t5.add(new LearningContent(
            "How Vaccines Protect Us",
            "Vaccines train your immune system to recognise and fight specific\n" +
            "viruses or bacteria without causing the disease itself.\n\n" +
            "They have:\n" +
            "  • Eliminated smallpox completely\n" +
            "  • Reduced polio by 99%\n" +
            "  • Dramatically cut measles deaths\n\n" +
            "Herd immunity occurs when enough people are vaccinated,\nprotecting those who cannot be vaccinated.",
            "Fact",
            "Vaccines prevent 2-3 million deaths every year\nworldwide according to the WHO.",
            "needle", "#378ADD", 1, "Vaccines",
            IMG + "07_vaccines.png"
        ));
        topics.put("Vaccines", t5);

        // ── Topic 6: Nutrition ────────────────────────────────────────────────
        List<LearningContent> t6 = new ArrayList<>();
        t6.add(new LearningContent(
            "Eating for Good Health",
            "A balanced diet includes:\n" +
            "  • Carbohydrates: main energy source (rice, bread, pasta)\n" +
            "  • Proteins: builds and repairs tissue (meat, eggs, beans)\n" +
            "  • Healthy fats: brain and hormone function (nuts, fish)\n" +
            "  • Vitamins and minerals: immune function and growth\n" +
            "  • Water: essential for all body processes\n\n" +
            "Both undernutrition and obesity are forms of malnutrition.",
            "Malaysian Context",
            "Over 50% of Malaysian adults are overweight or obese\naccording to the National Health and Morbidity Survey 2019.",
            "apple", "#639922", 1, "Nutrition",
            IMG + "08_nutrition.png"
        ));
        topics.put("Nutrition", t6);

        // ── Topic 7: Healthcare ───────────────────────────────────────────────
        List<LearningContent> t7 = new ArrayList<>();
        t7.add(new LearningContent(
            "Universal Health Coverage",
            "Universal Health Coverage (UHC) means everyone can access quality\n" +
            "health services without facing financial hardship.\n\n" +
            "SDG 3 targets UHC for all by 2030. Currently, at least half the\n" +
            "world's population lacks access to essential health services.\n\n" +
            "Malaysia has a dual public-private health system. Public hospitals\nare heavily subsidised by the government.",
            "Malaysia",
            "Malaysia's public hospitals charge minimal fees —\noutpatient visits cost as little as RM1 for citizens.",
            "building-hospital", "#BA7517", 1, "Healthcare",
            IMG + "09_healthcare.png"
        ));
        topics.put("Healthcare", t7);

        // ── Topic 8: Substance Harm ───────────────────────────────────────────
        List<LearningContent> t8 = new ArrayList<>();
        t8.add(new LearningContent(
            "Tobacco & Alcohol Harm",
            "Tobacco kills more than 8 million people per year globally.\n" +
            "About 1.2 million of those are non-smokers exposed to\nsecondhand smoke.\n\n" +
            "Alcohol causes 3 million deaths annually. Both tobacco and\n" +
            "alcohol are major risk factors for:\n" +
            "  • Multiple types of cancer\n" +
            "  • Heart and liver disease\n" +
            "  • Mental health disorders",
            "Fact",
            "Smoking is the single largest preventable cause of\ndeath and disease in the world.",
            "ban", "#E24B4A", 1, "Substance Harm",
            IMG + "10_substance.png"
        ));
        topics.put("Substance Harm", t8);

        // ── Topic 9: Malaysia Context ─────────────────────────────────────────
        List<LearningContent> t9 = new ArrayList<>();
        t9.add(new LearningContent(
            "Health in Malaysia",
            "Malaysia faces a double burden of disease — both infectious\n" +
            "diseases and rising NCDs.\n\n" +
            "Key health challenges include:\n" +
            "  • Diabetes: 1 in 5 Malaysian adults is diabetic\n" +
            "  • Hypertension: affects 1 in 3 adults\n" +
            "  • Obesity: over 50% of adults overweight\n\n" +
            "The government runs MySihat and the Skim Peduli Sihat\nprogram to support low-income households.",
            "Sarawak Note",
            "Rural communities in Sarawak face challenges in\nhealthcare access due to geographical barriers.",
            "map-pin", "#1D9E75", 1, "Malaysia Context",
            IMG + "11_malaysia.png"
        ));
        topics.put("Malaysia Context", t9);

        // ── Topic 10: Take Action ─────────────────────────────────────────────
        List<LearningContent> t10 = new ArrayList<>();
        t10.add(new LearningContent(
            "What You Can Do",
            "Every individual action counts toward SDG 3:\n" +
            "  • Sleep 7-9 hours each night\n" +
            "  • Eat balanced meals with fruits and vegetables\n" +
            "  • Exercise at least 30 minutes a day\n" +
            "  • Avoid tobacco and excessive alcohol\n" +
            "  • Seek mental health support when needed\n" +
            "  • Stay up to date with vaccinations\n\n" +
            "Share what you have learned — education and awareness\nare the first steps to change.",
            "Call to Action",
            "Talk to one person today about what you learned.\nAwareness is the first step toward a healthier world.",
            "hand", "#7F77DD", 1, "Take Action",
            IMG + "12_take_action.png"
        ));
        topics.put("Take Action", t10);

        return topics;
    }
}
