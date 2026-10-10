# معمارية التطبيق
## Application Architecture

### 🏗️ البنية العامة

```
┌─────────────────────────────────────────────┐
│         Jetpack Compose UI Layer            │
├─────────────────────────────────────────────┤
│      ViewModel + LiveData (Reactive)         │
├─────────────────────────────────────────────┤
│  Recognition  │  Math Engine  │  Plotting   │
│   (ML Model)  │   (Symbolic)  │   (Canvas)  │
├─────────────────────────────────────────────┤
│     Local Database (Room - SQLite)          │
├─────────────────────────────────────────────┤
│   Offline Resources (Models, Assets)        │
└─────────────────────────────────────────────┘
```

### 📦 الطبقات الرئيسية

#### 1. **Presentation Layer** (UI)
```
com.mathapp.ui/
├── screens/
│   ├── CanvasScreen.kt              # لوحة الرسم
│   ├── EquationScreen.kt            # عرض المعادلات
│   ├── GraphScreen.kt               # رسم التوابع
│   ├── HistoryScreen.kt             # السجل
│   └── SettingsScreen.kt            # الإعدادات
├── components/
│   ├── DrawingCanvas.kt             # مكون الرسم
│   ├── MathEquationView.kt          # عرض المعادلة
│   ├── GraphPlotter.kt              # رسام الرسوم البيانية
│   └── BottomToolbar.kt             # شريط الأدوات
└── theme/
    ├── Color.kt
    ├── Typography.kt
    └── Theme.kt
```

#### 2. **Domain Layer** (Logic)
```
com.mathapp.domain/
├── models/
│   ├── Equation.kt                  # نموذج المعادلة
│   ├── RecognitionResult.kt         # نتيجة التعرف
│   ├── MathExpression.kt            # التعبير الرياضي
│   └── Graph.kt                     # الرسم البياني
├── repositories/
│   ├── EquationRepository.kt
│   ├── RecognitionRepository.kt
│   └── GraphRepository.kt
└── usecases/
    ├── RecognizeHandwritingUC.kt
    ├── AnalyzeEquationUC.kt
    ├── PlotGraphUC.kt
    └── SolveEquationUC.kt
```

#### 3. **Data Layer** (Storage & External)
```
com.mathapp.data/
├── local/
│   ├── database/
│   │   ├── MathDatabase.kt
│   │   ├── EquationDao.kt
│   │   └── entities/
│   └── models/
├── remote/                          # (Not used - Offline)
└── datasources/
    ├── LocalEquationDataSource.kt
    └── LocalModelDataSource.kt
```

#### 4. **ML Recognition Layer**
```
com.mathapp.ml/
├── models/
│   ├── TFLiteInterpreter.kt         # محرك TensorFlow Lite
│   ├── ONNXInterpreter.kt           # محرك ONNX
│   └── ModelLoader.kt               # تحميل النماذج
├── preprocessing/
│   ├── ImagePreprocessor.kt         # معالجة الصور
│   ├── StrokeNormalizer.kt          # تنسيق السكتات
│   └── SegmentDetector.kt           # كشف الأجزاء
└── postprocessing/
    ├── SymbolPostprocessor.kt       # معالجة الرموز
    └── EquationAssembler.kt         # تجميع المعادلة
```

#### 5. **Math Engine Layer**
```
com.mathapp.math/
├── parser/
│   ├── LaTeXParser.kt               # تحليل LaTeX
│   ├── MathExpressionParser.kt      # تحليل التعبيرات
│   └── Tokenizer.kt                 # تقسيم الرموز
├── solver/
│   ├── LinearEquationSolver.kt      # حل المعادلات الخطية
│   ├── QuadraticSolver.kt           # حل المعادلات التربيعية
│   ├── PolynomialSolver.kt          # حل كثيرات الحدود
│   └── SymbolicSolver.kt            # حل رمزي
├── analyzer/
│   ├── DerivativeAnalyzer.kt        # حساب المشتقات
│   ├── IntegralAnalyzer.kt          # حساب التكاملات
│   ├── RootFinder.kt                # إيجاد الأصفار
│   └── ExtremaFinder.kt             # إيجاد النهايات
├── simplifier/
│   ├── AlgebraicSimplifier.kt       # تبسيط جبري
│   ├── TrigonometricSimplifier.kt   # تبسيط مثلثي
│   └── RationalSimplifier.kt        # تبسيط كسري
└── generator/
    ├── LaTeXGenerator.kt            # توليد LaTeX
    └── MathMLGenerator.kt           # توليد MathML
```

#### 6. **Plotting/Visualization Layer**
```
com.mathapp.plotting/
├── renderers/
│   ├── GraphRenderer.kt             # رسم الرسم البياني
│   ├── AxisRenderer.kt              # رسم المحاور
│   └── CurveRenderer.kt             # رسم المنحنيات
├── calculator/
│   ├── PointCalculator.kt           # حساب النقاط
│   ├── DomainCalculator.kt          # حساب المجال
│   └── RangeCalculator.kt           # حساب المدى
└── utils/
    ├── CoordinateTransformer.kt     # تحويل الإحداثيات
    └── ScaleCalculator.kt           # حساب المقياس
```

### 🔄 تدفق البيانات

#### سيناريو: رسم وتحويل معادلة

```
1. المستخدم يرسم على اللوحة
   ↓
2. DrawingCanvas يسجل السكتات (Strokes)
   ↓
3. ImagePreprocessor يحول السكتات إلى صورة
   ↓
4. TFLite Model يصنف الرموز
   ↓
5. SymbolPostprocessor يجمع الرموز في معادلة
   ↓
6. MathExpressionParser يحلل المعادلة
   ↓
7. LaTeXGenerator ينتج LaTeX
   ↓
8. UI يعرض النتيجة + الرسم البياني
   ↓
9. Database يحفظ التاريخ
```

### 💾 نموذج البيانات

#### Equation
```kotlin
@Entity
data class Equation(
    @PrimaryKey val id: String,
    val originalStroke: Bitmap,
    val rawLatex: String,
    val simplifiedLatex: String,
    val mathML: String,
    val timestamp: Long,
    val category: String,      // linear, quadratic, trigonometric...
    val confidence: Float
)
```

#### RecognitionResult
```kotlin
data class RecognitionResult(
    val symbols: List<Symbol>,
    val equation: MathExpression,
    val latex: String,
    val confidence: Float,
    val processingTime: Long
)
```

#### Graph
```kotlin
data class Graph(
    val expression: MathExpression,
    val points: List<Point>,
    val domain: Pair<Double, Double>,
    val range: Pair<Double, Double>,
    val properties: GraphProperties
)

data class GraphProperties(
    val roots: List<Double>,
    val extrema: List<Extremum>,
    val inflectionPoints: List<Point>,
    val asymptotes: List<Line>
)
```

### 🎯 نمط MVVM مع Coroutines

```kotlin
class CanvasViewModel : ViewModel() {
    private val _recognitionResult = MutableLiveData<RecognitionResult>()
    val recognitionResult: LiveData<RecognitionResult> = _recognitionResult
    
    fun recognizeDrawing(bitmap: Bitmap) = viewModelScope.launch {
        val result = recognitionUseCase(bitmap)
        _recognitionResult.value = result
    }
}
```

### 🔌 تحميل النماذج

النماذج تُحمل مرة واحدة عند بدء التطبيق:

```kotlin
class ModelManager {
    private val tfliteInterpreter = TFLiteInterpreter(context)
    private val onnxRuntime = ONNXInterpreter(context)
    
    init {
        tfliteInterpreter.loadModel("equation_recognition.tflite")
        onnxRuntime.loadModel("symbol_classifier.onnx")
    }
}
```

### ⚙️ الإعتماديات الرئيسية

```gradle
dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    
    // Compose
    implementation 'androidx.compose.ui:ui:1.6.0'
    implementation 'androidx.compose.material3:material3:1.1.0'
    
    // ML & Vision
    implementation 'org.tensorflow:tensorflow-lite:2.13.0'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.13.0'
    implementation 'com.microsoft.onnxruntime:onnxruntime-android:1.14.0'
    implementation 'org.opencv:opencv-android:4.8.0'
    
    // Database
    implementation 'androidx.room:room-runtime:2.5.2'
    
    // Graphics
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    
    // Math
    implementation 'org.apache.commons:commons-math3:3.6.1'
    
    // Async
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
}
```

### 🚀 التدفق الأمثل للأداء

1. **استخدام GPU للنماذج** عبر TensorFlow Lite GPU Delegate
2. **معالجة الصور في Background** مع Coroutines
3. **Caching للنتائج** المحسوبة
4. **تحميل النماذج مرة واحدة** في Singleton
5. **حفظ غير متزامن** للقاعدة البيانات
