package com.example.miappcompose

import androidx.compose.runtime.saveable.rememberSaveable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalView

data class Paleta(
    val bg: Color,
    val displayBg: Color,
    val displayText: Color,
    val keyBg: Color,
    val keyText: Color,
    val equalsBg: Color,
    val equalsText: Color,
    val accent: Color,
    val iconos: Color
)

val PaletaDia = Paleta(
    bg = Color(0xFFAECDE3),
    displayBg = Color(0xFFFFFFFF),
    displayText = Color(0xFF9B7BB8),
    keyBg = Color(0xFFA58EB5),
    keyText = Color(0xFFFFF3C4),
    equalsBg = Color(0xFFFFD1EA),
    equalsText = Color(0xFF6B2FA0),
    accent = Color(0xFF6B2FA0),
    iconos = Color(0xFFFFF3C4)
)

val PaletaNoche = Paleta(
    bg = Color(0xFF0B1F3A),
    displayBg = Color(0xFF313B6B),
    displayText = Color(0xFF9B7BB8),
    keyBg = Color(0xFF8B72A6),
    keyText = Color(0xFFFFF3C4),
    equalsBg = Color(0xFF8F6F6B),
    equalsText = Color(0xFFFFF3C4),
    accent = Color(0xFFB08CE0),
    iconos = Color(0xFFC9B4F0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CalkApp() }
    }
}

enum class Pantalla { ENTRADA, CALCULADORA }

@Composable
fun CalkApp() {
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.ENTRADA) }
    var modoNoche by rememberSaveable { mutableStateOf(false) }
    val paleta = if (modoNoche) PaletaNoche else PaletaDia

    when (pantalla) {
        Pantalla.ENTRADA -> PantallaEntrada(
            paleta = paleta,
            onEmpezar = { pantalla = Pantalla.CALCULADORA }
        )
        Pantalla.CALCULADORA -> PantallaCalculadora(
            paleta = paleta,
            modoNoche = modoNoche,
            onCambiarModo = { modoNoche = !modoNoche },
            onAtras = { pantalla = Pantalla.ENTRADA }
        )
    }
}
@Composable
fun PantallaEntrada(paleta: Paleta, onEmpezar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.bg)
            .systemBarsPadding()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "¡Bienvenido/a a Calk!",
            color = paleta.accent,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Creado por Andrea G 2ºDAM",
            color = paleta.accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(paleta.keyBg)
                .clickable { onEmpezar() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Empieza a calcular",
                color = paleta.keyText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PantallaCalculadora(
    paleta: Paleta,
    modoNoche: Boolean,
    onCambiarModo: () -> Unit,
    onAtras: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(paleta.bg)
            .systemBarsPadding()
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoBoton(R.drawable.ic_back, paleta.iconos, onAtras)
            Spacer(Modifier.weight(1f))
            IconoBoton(
                if (modoNoche) R.drawable.themeday else R.drawable.ic_theme,
                paleta.iconos,
                onCambiarModo
            )
            Spacer(Modifier.width(8.dp))
            IconoBoton(R.drawable.ic_settings, paleta.iconos) { }
        }

        Spacer(Modifier.height(10.dp))

        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .clip(RoundedCornerShape(20.dp))
                .background(paleta.displayBg)
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "0",
                color = paleta.displayText,
                fontSize = 36.sp,
                textAlign = TextAlign.End
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilaTeclas(listOf("(", ")", "AC"), paleta, Modifier.weight(1f))
            FilaTeclas(listOf("C", "÷", "×", "⌫"), paleta, Modifier.weight(1f))
            FilaTeclas(listOf("1", "2", "3", "−"), paleta, Modifier.weight(1f))
            FilaTeclas(listOf("4", "5", "6", "+"), paleta, Modifier.weight(1f))

            Row(
                modifier = Modifier.weight(2f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(3f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilaTeclas(listOf("7", "8", "9"), paleta, Modifier.weight(1f))
                    FilaTeclas(listOf("%", "0", ","), paleta, Modifier.weight(1f))
                }
                Tecla(
                    texto = "=",
                    fondo = paleta.equalsBg,
                    colorTexto = paleta.equalsText,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun IconoBoton(recurso: Int, tint: Color, onClick: () -> Unit) {
    Icon(
        painter = painterResource(recurso),
        contentDescription = null,
        tint = tint,
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() }
    )
}

@Composable
fun FilaTeclas(
    teclas: List<String>,
    paleta: Paleta,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        teclas.forEach { t ->
            Tecla(
                texto = t,
                fondo = paleta.keyBg,
                colorTexto = paleta.keyText,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

@Composable
fun Tecla(
    texto: String,
    fondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(fondo)
            .clickable {
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = texto, color = colorTexto, fontSize = 22.sp)
    }
}
