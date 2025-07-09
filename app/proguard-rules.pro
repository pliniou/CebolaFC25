# Regras de Proguard para o aplicativo CebolaFC25

# Manter classes anotadas com @Keep
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# Hilt - Injeção de Dependência
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint { <init>(); }
-keep class * implements dagger.hilt.internal.GeneratedComponent { <init>(); }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { <init>(); }
-keep class * implements dagger.hilt.internal.GeneratedSubcomponent { <init>(); }
-keep class dagger.hilt.internal.processedroots.*
-keep class com.example.cebolafc25.Hilt_** { *; }
-keep class hilt_aggregated_deps.** { *; }

# Room - Banco de Dados Local
-keep class com.example.cebolafc25.data.model.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.util.TableInfo
-keepclassmembers class * extends androidx.room.TypeConverter {
    public <methods>;
}

# GSON - Parsing de JSON
# Manter os nomes dos campos nos modelos de dados que são usados pelo Gson
-keepclassmembers class com.example.cebolafc25.util.LeagueJson {
    <fields>;
}
-keepclassmembers class com.example.cebolafc25.util.TeamJson {
    <fields>;
}
-keepattributes Signature
-keepattributes *Annotation*

# Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherFactory {
    private static final kotlinx.coroutines.MainCoroutineDispatcher a;
    public static final kotlinx.coroutines.internal.MainDispatcherFactory INSTANCE;
}
-keepclassmembers class kotlin.coroutines.jvm.internal.BaseContinuationImpl {
    <init>(...);
    kotlin.coroutines.Continuation getCompletion();
    java.lang.Object invokeSuspend(java.lang.Object);
}
-keepclassmembers class kotlin.coroutines.jvm.internal.ContinuationImpl {
    <init>(...);
}
-keepclassmembers class kotlin.coroutines.jvm.internal.DebugProbesKt {
    <methods>;
}
-keepnames class kotlin.coroutines.Continuation
-keepnames class kotlinx.coroutines.Job