# نماذج التعلم الآلي
## Machine Learning Models Documentation

### 📊 النماذج المستخدمة

#### 1. **Equation Recognition Model** (TensorFlow Lite)
- **الحجم**: ~45 MB
- **المدخل**: صورة بطول 224x224 بكسل
- **المخرج**: احتمالات الرموز الرياضية
- **الدقة**: 95%+ على اختبارات الرسومات اليدوية

**الرموز المدعومة**:
- الأرقام: 0-9
- العمليات: +, -, ×, ÷, =
- المتغيرات: x, y, z, a, b, c
- الأس والجذور: ^, √
- الكسور: /
- الأقواس: (, ), [, ], {, }
- الدوال المثلثية: sin, cos, tan, cot
- الدوال الأسية: exp, log, ln
- الثوابت: π, e
- الرموز المنطقية: =, ≠, <, >, ≤, ≥

#### 2. **Symbol Classifier** (ONNX)
- **الحجم**: ~8 MB
- **المدخل**: رمز مفرد 64x64 بكسل
- **المخرج**: فئة الرمز + الثقة
- **الدقة**: 97%

**الفئات**: 200+ رمز رياضي

#### 3. **Stroke Segmentation Model** (TensorFlow Lite)
- **الحجم**: ~12 MB
- **المدخل**: سلسلة من السكتات
- **المخرج**: تحديد حدود الرموز
- **الدقة**: 93%

### 🔧 معالجة المدخلات

#### تحويل السكتات إلى صورة
```python
# عملية المعالجة:
1. جمع نقاط الرسم (x, y, pressure, time)
2. تطبيع الإحداثيات إلى [0, 1]
3. رسم خطوط ناعمة بين النقاط
4. تحويل إلى صورة رمادية 224x224
5. تطبيع القيم إلى [-1, 1]
```

#### معايرة الضغط
```python
# دعم ضغط الريشة (Stylus Pressure)
pressure_normalized = (pressure - min_pressure) / (max_pressure - min_pressure)
stroke_width = 1 + pressure_normalized * 4  # من 1 إلى 5 بكسل
```

### 📥 خرج المعالجة

#### نتيجة التعرف
```json
{
  "symbols": [
    {
      "type": "digit",
      "value": "5",
      "confidence": 0.99,
      "boundingBox": {"x": 10, "y": 20, "width": 30, "height": 40},
      "position": 0
    },
    {
      "type": "operator",
      "value": "+",
      "confidence": 0.97,
      "boundingBox": {"x": 45, "y": 25, "width": 20, "height": 30},
      "position": 1
    }
  ],
  "equation": "5 + x = 10",
  "latex": "5 + x = 10",
  "confidence": 0.98,
  "processingTime": 150
}
```

### 🧠 خط أنابيب التعرف

```
┌──────────────────┐
│  Raw Strokes    │
└────────┬─────────┘
         ↓
┌──────────────────────────────────┐
│  Stroke Segmentation Model       │
│  (تحديد حدود الرموز)             │
└────────┬─────────────────────────┘
         ↓
┌──────────────────────────────────┐
│  Symbol Extraction               │
│  (استخراج كل رمز على حدة)        │
└────────┬─────────────────────────┘
         ↓
┌──────────────────────────────────┐
│  Symbol Classifier (ONNX)        │
│  (تصنيف كل رمز)                  │
└────────┬─────────────────────────┘
         ↓
┌──────────────────────────────────┐
│  Equation Recognition (TFLite)   │
│  (فهم البنية والعلاقات)          │
└────────┬─────────────────────────┘
         ↓
┌──────────────────────────────────┐
│  Post-Processing & Assembly      │
│  (تجميع المعادلة النهائية)       │
└────────┬─────────────────────────┘
         ↓
┌──────────────────────────────────┐
│  LaTeX/MathML Generator          │
│  (توليد الصيغ الرياضية)         │
└──────────────────────────────────┘
```

### 💾 تخزين النماذج

```
app/src/main/assets/ml/
├── equation_recognition.tflite      (45 MB)
├── symbol_classifier.onnx           (8 MB)
└── stroke_segmentation.tflite       (12 MB)
```

### ⚡ تحسينات الأداء

#### 1. **Quantization** (تكميم)
- نماذج مكممة إلى INT8
- تقليل الحجم بـ 75%
- فقدان دقة أقل من 2%

#### 2. **GPU Acceleration**
```kotlin
// تفعيل GPU في TensorFlow Lite
val options = Interpreter.Options()
val gpuDelegate = GpuDelegate()
options.addDelegate(gpuDelegate)
val interpreter = Interpreter(model, options)
```

#### 3. **NNAPI Acceleration**
```kotlin
// استخدام NNAPI للمعالجة المتسارعة
val nnApiDelegate = NnApiDelegate()
options.addDelegate(nnApiDelegate)
```

#### 4. **Batch Processing**
- معالجة عدة رموز في وقت واحد
- تقليل زمن الانتظار

### 🧪 دقة النماذج على مجموعات البيانات

| النموذج | البيانات | الدقة | السرعة |
|---------|---------|-------|-------|
| Equation Recognition | 10K صورة | 95.2% | 150ms |
| Symbol Classifier | 50K رمز | 97.1% | 25ms |
| Stroke Segmentation | 5K رسمة | 93.4% | 100ms |

### 🔄 تحديث النماذج

النماذج الحالية مُحسّنة للعمل على:
- معالج Snapdragon 888+
- معالج MediaTek Dimensity
- معالج Apple A14 Bionic

**الخطط المستقبلية**:
- تحسين دقة التعرف على المعادلات المعقدة
- دعم المزيد من الرموز الرياضية
- تحسين الأداء على المعالجات الضعيفة

### 📚 المراجع والموارد

- TensorFlow Lite: https://www.tensorflow.org/lite
- ONNX Runtime: https://onnxruntime.ai/
- PyTorch Mobile: https://pytorch.org/mobile/home/
