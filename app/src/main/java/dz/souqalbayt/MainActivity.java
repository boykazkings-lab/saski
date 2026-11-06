package dz.souqalbayt;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int GREEN = Color.rgb(54, 94, 75);
    private static final int DARK = Color.rgb(41, 42, 37);
    private static final int MUTED = Color.rgb(126, 132, 123);
    private static final int CREAM = Color.rgb(250, 248, 243);
    private static final int LINE = Color.rgb(234, 232, 225);
    private static final int PALE = Color.rgb(233, 240, 233);
    private static final String PREFS = "souq_albayt_local";

    private final List<Product> products = new ArrayList<>();
    private String selectedCategory = "الكل";
    private String query = "";
    private SharedPreferences prefs;
    private EditText phoneInput;
    private EditText searchInput;
    private LinearLayout productList;
    private TextView totalView;
    private TextView countView;

    private static final String[] CATEGORIES = {
            "الكل", "مواد غذائية", "خضر وفواكه", "حليب وأجبان", "لحوم وأسماك",
            "تنظيف المنزل", "المطبخ", "عناية شخصية", "البيت والديكور",
            "الأطفال والرضع", "مفروشات", "صيدلية", "أجهزة صغيرة", "حيوانات أليفة"
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        getWindow().setStatusBarColor(CREAM);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        seedProducts();
        buildScreen();
    }

    private void seedProducts() {
        addProduct("milk", "حليب طويل الأمد", "حليب وأجبان", "🥛", "علبة 1 لتر", 150);
        addProduct("eggs", "بيض طازج", "مواد غذائية", "🥚", "طبق 30 حبة", 620);
        addProduct("oil", "زيت المائدة", "مواد غذائية", "🫒", "قارورة 2 لتر", 650);
        addProduct("olive", "زيت زيتون بكر", "مواد غذائية", "🫒", "قارورة 1 لتر", 1450);
        addProduct("bread", "خبز", "مواد غذائية", "🥖", "3 أرغفة", 45);
        addProduct("rice", "أرز طويل الحبة", "مواد غذائية", "🍚", "كيس 1 كغ", 320);
        addProduct("sugar", "سكر", "مواد غذائية", "🍚", "كيس 1 كغ", 120);
        addProduct("pasta", "معكرونة", "مواد غذائية", "🍝", "كيسان", 160);
        addProduct("coffee", "قهوة مطحونة", "مواد غذائية", "☕", "عبوة 250 غ", 690);
        addProduct("potato", "بطاطا", "خضر وفواكه", "🥔", "للكيلوغرام", 180);
        addProduct("tomato", "طماطم", "خضر وفواكه", "🍅", "للكيلوغرام", 180);
        addProduct("apple", "تفاح أحمر", "خضر وفواكه", "🍎", "للكيلوغرام", 380);
        addProduct("banana", "موز", "خضر وفواكه", "🍌", "للكيلوغرام", 350);
        addProduct("yogurt", "ياغورت طبيعي", "حليب وأجبان", "🥣", "6 علب", 360);
        addProduct("cheese", "جبن طري", "حليب وأجبان", "🧀", "علبة 500 غ", 360);
        addProduct("chicken", "دجاج", "لحوم وأسماك", "🍗", "للكيلوغرام", 850);
        addProduct("fish", "سمك", "لحوم وأسماك", "🐟", "للكيلوغرام", 1200);
        addProduct("cleaner", "منظف متعدد الاستعمال", "تنظيف المنزل", "🧴", "عبوة 1 لتر", 420);
        addProduct("dish", "سائل غسل الأواني", "تنظيف المنزل", "🫧", "عبوة 750 مل", 290);
        addProduct("laundry", "مسحوق غسيل الملابس", "تنظيف المنزل", "🧺", "عبوة 2 كغ", 1150);
        addProduct("shampoo", "شامبو للشعر", "عناية شخصية", "🧴", "عبوة 400 مل", 690);
        addProduct("paste", "معجون أسنان", "عناية شخصية", "🪥", "عبوة 100 مل", 280);
        addProduct("pan", "طقم أواني طبخ", "المطبخ", "🍲", "5 قطع", 6800);
        addProduct("boxes", "علب حفظ الطعام", "المطبخ", "🥡", "5 علب", 1250);
        addProduct("organizer", "منظم أدراج", "البيت والديكور", "🗂️", "قطعتان", 750);
        addProduct("lamp", "مصباح طاولة", "البيت والديكور", "💡", "قطعة واحدة", 3200);
        addProduct("diapers", "حفاضات أطفال", "الأطفال والرضع", "🧸", "عبوة 40 قطعة", 1850);
        addProduct("blanket", "طقم أغطية سرير", "مفروشات", "🛏️", "3 قطع", 4900);
        addProduct("firstaid", "علبة إسعافات أولية", "صيدلية", "🩹", "طقم منزلي", 1250);
        addProduct("blender", "خلاط كهربائي", "أجهزة صغيرة", "🥤", "600 واط", 5900);
        addProduct("catfood", "طعام قطط جاف", "حيوانات أليفة", "🐾", "كيس 1 كغ", 980);
    }

    private void addProduct(String id, String name, String category, String emoji, String unit, int price) {
        products.add(new Product(id, name, category, emoji, unit, price));
    }

    private void buildScreen() {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        page.setBackgroundColor(CREAM);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(18), dp(12), dp(18), dp(18));
        body.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        LinearLayout brandRow = new LinearLayout(this);
        brandRow.setGravity(Gravity.CENTER_VERTICAL);
        brandRow.setOrientation(LinearLayout.HORIZONTAL);
        TextView mark = text("⌂", 25, Color.WHITE, true);
        mark.setGravity(Gravity.CENTER);
        mark.setBackground(round(GREEN, 16, 0));
        brandRow.addView(mark, new LinearLayout.LayoutParams(dp(48), dp(48)));
        LinearLayout brandText = new LinearLayout(this);
        brandText.setOrientation(LinearLayout.VERTICAL);
        brandText.setPadding(dp(12), 0, 0, 0);
        brandText.addView(text("سوق البيت", 21, DARK, true));
        brandText.addView(text("اختاري احتياجات البيت وأرسليها برسالة", 12, MUTED, false));
        brandRow.addView(brandText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        body.addView(brandRow);

        TextView intro = text("قائمة البيت، برسالة واحدة ✉️", 18, GREEN, true);
        intro.setPadding(0, dp(22), 0, dp(4));
        body.addView(intro);
        TextView hint = text("اختاري المنتجات والكميات، ثم أرسليها إلى هاتفك برسالة SMS.", 13, MUTED, false);
        body.addView(hint);

        TextView phoneLabel = text("رقم هاتفك لاستقبال القائمة", 12, DARK, true);
        phoneLabel.setPadding(0, dp(18), 0, dp(7));
        body.addView(phoneLabel);
        phoneInput = new EditText(this);
        phoneInput.setSingleLine(true);
        phoneInput.setTextSize(15);
        phoneInput.setHint("مثال: +213 5XX XX XX XX");
        phoneInput.setInputType(3);
        phoneInput.setText(prefs.getString("phone", ""));
        phoneInput.setPadding(dp(14), 0, dp(14), 0);
        phoneInput.setBackground(round(Color.WHITE, 13, LINE));
        phoneInput.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        phoneInput.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        body.addView(phoneInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(50)));
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { prefs.edit().putString("phone", s.toString()).apply(); }
            @Override public void afterTextChanged(Editable s) {}
        });
        TextView privateNote = text("يُحفظ الرقم والسلة على هذا الهاتف فقط.", 10, MUTED, false);
        privateNote.setPadding(0, dp(5), 0, dp(12));
        body.addView(privateNote);

        searchInput = new EditText(this);
        searchInput.setSingleLine(true);
        searchInput.setTextSize(14);
        searchInput.setHint("ابحثي عن الحليب أو البيض أو الزيت…");
        searchInput.setPadding(dp(14), 0, dp(14), 0);
        searchInput.setBackground(round(Color.WHITE, 13, LINE));
        body.addView(searchInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(48)));

        HorizontalScrollView categoryScroll = new HorizontalScrollView(this);
        categoryScroll.setHorizontalScrollBarEnabled(false);
        categoryScroll.setPadding(0, dp(14), 0, dp(11));
        LinearLayout categoryRow = new LinearLayout(this);
        categoryRow.setOrientation(LinearLayout.HORIZONTAL);
        categoryRow.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        for (String category : CATEGORIES) {
            TextView chip = text(category, 12, GREEN, true);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(dp(14), dp(9), dp(14), dp(9));
            chip.setBackground(round(category.equals(selectedCategory) ? PALE : Color.WHITE, 22, category.equals(selectedCategory) ? GREEN : LINE));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(38));
            cp.setMargins(0, 0, dp(7), 0);
            categoryRow.addView(chip, cp);
            chip.setOnClickListener(v -> { selectedCategory = category; updateCategoryChips(categoryRow); renderProducts(); });
        }
        categoryScroll.addView(categoryRow);
        body.addView(categoryScroll);

        TextView section = text("احتياجات البيت", 18, DARK, true);
        section.setPadding(0, dp(3), 0, dp(4));
        body.addView(section);
        TextView estimate = text("الأسعار المعروضة تقريبية للتجربة", 11, MUTED, false);
        body.addView(estimate);

        productList = new LinearLayout(this);
        productList.setOrientation(LinearLayout.VERTICAL);
        productList.setPadding(0, dp(10), 0, 0);
        body.addView(productList);
        scroll.addView(body);
        page.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.setGravity(Gravity.CENTER_VERTICAL);
        bottom.setPadding(dp(17), dp(12), dp(17), dp(12));
        bottom.setBackgroundColor(Color.WHITE);
        TextView summary = text("المجموع التقريبي", 11, MUTED, false);
        totalView = text("0 دج", 18, GREEN, true);
        countView = text("0 منتجات", 11, MUTED, false);
        LinearLayout totals = new LinearLayout(this);
        totals.setOrientation(LinearLayout.VERTICAL);
        totals.addView(summary);
        totals.addView(totalView);
        totals.addView(countView);
        bottom.addView(totals, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView send = text("إرسال SMS  ✉", 14, Color.WHITE, true);
        send.setGravity(Gravity.CENTER);
        send.setPadding(dp(15), 0, dp(15), 0);
        send.setBackground(round(GREEN, 13, 0));
        bottom.addView(send, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(49)));
        send.setOnClickListener(v -> sendSms());
        page.addView(bottom);

        setContentView(page);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { query = s.toString().trim().toLowerCase(new Locale("ar")); renderProducts(); }
            @Override public void afterTextChanged(Editable s) {}
        });
        renderProducts();
        updateTotal();
    }

    private void updateCategoryChips(LinearLayout row) {
        for (int i = 0; i < row.getChildCount(); i++) {
            TextView chip = (TextView) row.getChildAt(i);
            boolean active = chip.getText().toString().equals(selectedCategory);
            chip.setTextColor(GREEN);
            chip.setBackground(round(active ? PALE : Color.WHITE, 22, active ? GREEN : LINE));
        }
    }

    private void renderProducts() {
        if (productList == null) return;
        productList.removeAllViews();
        int shown = 0;
        for (Product p : products) {
            boolean categoryMatch = selectedCategory.equals("الكل") || selectedCategory.equals(p.category);
            String searchable = (p.name + " " + p.category + " " + p.unit).toLowerCase(new Locale("ar"));
            if (!categoryMatch || !searchable.contains(query)) continue;
            shown++;
            productList.addView(productRow(p));
        }
        if (shown == 0) {
            TextView empty = text("لا توجد منتجات في هذا القسم.", 13, MUTED, false);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, dp(30), 0, dp(30));
            productList.addView(empty);
        }
        updateTotal();
    }

    private View productRow(Product p) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(12), dp(10), dp(12), dp(10));
        card.setBackground(round(Color.WHITE, 15, LINE));
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(9));
        card.setLayoutParams(cardParams);

        TextView emoji = text(p.emoji, 24, DARK, false);
        emoji.setGravity(Gravity.CENTER);
        emoji.setBackground(round(PALE, 12, 0));
        card.addView(emoji, new LinearLayout.LayoutParams(dp(45), dp(45)));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(dp(10), 0, dp(8), 0);
        TextView name = text(p.name, 14, DARK, true);
        TextView detail = text(p.category + " · " + p.unit, 10, MUTED, false);
        TextView price = text(formatPrice(p.price) + " دج", 12, GREEN, true);
        info.addView(name);
        info.addView(detail);
        info.addView(price);
        card.addView(info, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout qtyBox = new LinearLayout(this);
        qtyBox.setOrientation(LinearLayout.HORIZONTAL);
        qtyBox.setGravity(Gravity.CENTER);
        qtyBox.setBackground(round(PALE, 11, 0));
        int qty = prefs.getInt("qty_" + p.id, 0);
        if (qty == 0) {
            TextView add = text("＋", 23, GREEN, true);
            add.setGravity(Gravity.CENTER);
            qtyBox.addView(add, new LinearLayout.LayoutParams(dp(38), dp(38)));
        } else {
            TextView minus = text("−", 19, GREEN, true);
            minus.setGravity(Gravity.CENTER);
            TextView number = text(String.valueOf(qty), 13, DARK, true);
            number.setGravity(Gravity.CENTER);
            TextView plus = text("＋", 19, GREEN, true);
            plus.setGravity(Gravity.CENTER);
            qtyBox.addView(minus, new LinearLayout.LayoutParams(dp(28), dp(38)));
            qtyBox.addView(number, new LinearLayout.LayoutParams(dp(22), dp(38)));
            qtyBox.addView(plus, new LinearLayout.LayoutParams(dp(28), dp(38)));
            minus.setOnClickListener(v -> changeQty(p, -1));
            plus.setOnClickListener(v -> changeQty(p, 1));
        }
        if (qty == 0) qtyBox.setOnClickListener(v -> changeQty(p, 1));
        card.addView(qtyBox);
        return card;
    }

    private void changeQty(Product product, int delta) {
        int qty = Math.max(0, prefs.getInt("qty_" + product.id, 0) + delta);
        prefs.edit().putInt("qty_" + product.id, qty).apply();
        renderProducts();
    }

    private void updateTotal() {
        if (totalView == null) return;
        int total = 0, count = 0;
        for (Product p : products) {
            int qty = prefs.getInt("qty_" + p.id, 0);
            total += p.price * qty;
            count += qty;
        }
        totalView.setText(formatPrice(total) + " دج");
        countView.setText(count + " منتجات في السلة");
    }

    private void sendSms() {
        String rawPhone = phoneInput.getText().toString().trim();
        String phone = rawPhone.replaceAll("[^0-9+]", "");
        if (phone.isEmpty()) {
            phoneInput.setError("أدخلي رقم هاتفك أولًا");
            phoneInput.requestFocus();
            return;
        }
        StringBuilder message = new StringBuilder("قائمة مشتريات البيت:\n");
        int total = 0, count = 0;
        for (Product p : products) {
            int qty = prefs.getInt("qty_" + p.id, 0);
            if (qty <= 0) continue;
            count += qty;
            total += p.price * qty;
            message.append("- ").append(p.name).append(" ( ").append(qty).append(" ").append(p.unit).append(" )\n");
        }
        if (count == 0) {
            Toast.makeText(this, "أضيفي منتجًا واحدًا على الأقل إلى السلة", Toast.LENGTH_SHORT).show();
            return;
        }
        message.append("\nالمجموع التقريبي، والأسعار للتجربة: ").append(formatPrice(total)).append(" دج");
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(phone, "+")));
        intent.putExtra("sms_body", message.toString());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "لا يوجد تطبيق رسائل SMS مثبت على الهاتف", Toast.LENGTH_LONG).show();
        }
    }

    private String formatPrice(int value) {
        return NumberFormat.getNumberInstance(new Locale("ar", "DZ")).format(value);
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        return view;
    }

    private GradientDrawable round(int color, int radiusDp, int stroke) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        if (stroke != 0) drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class Product {
        final String id, name, category, emoji, unit;
        final int price;
        Product(String id, String name, String category, String emoji, String unit, int price) {
            this.id=id; this.name=name; this.category=category; this.emoji=emoji; this.unit=unit; this.price=price;
        }
    }
}
