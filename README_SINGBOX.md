# نسخه واقعی با هسته sing-box (توصیه شده)

این نسخه از **sing-box** به عنوان هسته VPN استفاده می‌کند.
sing-box مدرن، سریع و از تمام پروتکل‌های رایج (VLESS, VMess, Trojan, WireGuard, Shadowsocks و ...) پشتیبانی می‌کند.

## مراحل راه‌اندازی

### 1. اضافه کردن dependency

در فایل `app/build.gradle` این خط را اضافه کنید:

```groovy
implementation 'com.github.SagerNet:sing-box:1.8.0'
```

### 2. اضافه کردن مجوزها

در `AndroidManifest.xml` اضافه کنید:

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

### 3. استفاده از هسته

کد کامل در پوشه `singbox/` قرار داده شده است.

---

## نحوه کار

- سابسکریپشن دانلود می‌شود
- هر کانفیگ به فرمت JSON sing-box تبدیل می‌شود
- هسته sing-box راه‌اندازی و اتصال برقرار می‌شود

---

**نکته مهم:** 
برای استفاده کامل، باید از کتابخانه sing-box Android استفاده کنید.
در حال حاضر اسکلت کامل و آماده اتصال واقعی آماده است.