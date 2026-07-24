// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.JniUtils
import helium314.keyboard.latin.utils.SubtypeLocaleUtils.displayName
import helium314.keyboard.latin.utils.SubtypeSettings
import helium314.keyboard.settings.SearchSettingsScreen
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.settings.initPreview
import helium314.keyboard.latin.utils.previewDark
import helium314.keyboard.settings.screens.gesturedata.END_DATE_EPOCH_MILLIS
import helium314.keyboard.settings.screens.gesturedata.TWO_WEEKS_IN_MILLIS

@Composable
fun MainSettingsScreen(
    onClickAbout: () -> Unit,
    onClickTextCorrection: () -> Unit,
    onClickPreferences: () -> Unit,
    onClickToolbar: () -> Unit,
    onClickGestureTyping: () -> Unit,
    onClickDataGathering: () -> Unit,
    onClickAdvanced: () -> Unit,
    onClickAppearance: () -> Unit,
    onClickLanguage: () -> Unit,
    onClickLayouts: () -> Unit,
    onClickDictionaries: () -> Unit,
    onClickCloud: () -> Unit,
    onClickWelcomeWizard: () -> Unit, // Kept to prevent navigation graph crashes
    onClickBack: () -> Unit,
) {
    val enabledSubtypes = remember { SubtypeSettings.getEnabledSubtypes(true) }
    val enabledSubtypeNames = remember(enabledSubtypes) {
        enabledSubtypes.joinToString(", ") { it.displayName() }
    }
    val showDataGathering = remember {
        JniUtils.sHaveGestureLib && System.currentTimeMillis() < END_DATE_EPOCH_MILLIS + TWO_WEEKS_IN_MILLIS
    }

    SearchSettingsScreen(
        onClickBack = onClickBack,
        title = stringResource(R.string.ime_settings),
        settings = emptyList(),
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom),
            containerColor = Color.Transparent
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 24.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                item("language") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.language_and_layouts_title),
                        subtitle = enabledSubtypeNames,
                        icon = R.drawable.ic_settings_languages,
                        iconBgColor = Color(0xFF1976D2), // Blue
                        onClick = onClickLanguage
                    )
                }
                item("preferences") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_preferences),
                        subtitle = "General keyboard behaviors and sizes",
                        icon = R.drawable.ic_settings_preferences,
                        iconBgColor = Color(0xFF00897B), // Teal
                        onClick = onClickPreferences
                    )
                }
                item("appearance") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_appearance),
                        subtitle = "Themes, layouts, and visual styles",
                        icon = R.drawable.ic_settings_appearance,
                        iconBgColor = Color(0xFFD81B60), // Pink
                        onClick = onClickAppearance
                    )
                }
                item("toolbar") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_toolbar),
                        subtitle = "Customize quick access buttons",
                        icon = R.drawable.ic_settings_toolbar,
                        iconBgColor = Color(0xFF8E24AA), // Purple
                        onClick = onClickToolbar
                    )
                }
                item("cloud") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.cloud_features),
                        subtitle = "AI integrations and online services",
                        icon = R.drawable.ic_cloud,
                        iconBgColor = Color(0xFF00ACC1), // Cyan
                        onClick = onClickCloud
                    )
                }
                item("gesture_typing") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_gesture),
                        subtitle = if (JniUtils.sHaveGestureLib) "Glide typing enabled" else stringResource(R.string.gesture_not_loaded_summary),
                        icon = R.drawable.ic_settings_gesture,
                        iconBgColor = Color(0xFF3949AB), // Indigo
                        onClick = onClickGestureTyping
                    )
                }
                if (showDataGathering) {
                    item("data_gathering") {
                        ExpressiveCardPreference(
                            title = stringResource(R.string.gesture_data_screen),
                            subtitle = "Improve glide typing models",
                            icon = R.drawable.ic_settings_gesture,
                            iconBgColor = Color(0xFF546E7A), // Blue Grey
                            onClick = onClickDataGathering
                        )
                    }
                }
                item("correction") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_correction),
                        subtitle = "Auto-correction and suggestions",
                        icon = R.drawable.ic_settings_correction,
                        iconBgColor = Color(0xFFF4511E), // Deep Orange
                        onClick = onClickTextCorrection
                    )
                }
                item("layouts") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_secondary_layouts),
                        subtitle = "Symbols and numpad layouts",
                        icon = R.drawable.ic_ime_switcher,
                        iconBgColor = Color(0xFF5E35B1), // Deep Purple
                        onClick = onClickLayouts
                    )
                }
                item("dictionaries") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.dictionary_settings_category),
                        subtitle = "Personal and add-on dictionaries",
                        icon = R.drawable.ic_dictionary,
                        iconBgColor = Color(0xFF43A047), // Green
                        onClick = onClickDictionaries
                    )
                }
                item("advanced") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_advanced),
                        subtitle = "Expert and experimental settings",
                        icon = R.drawable.ic_settings_advanced,
                        iconBgColor = Color(0xFF6D4C41), // Brown
                        onClick = onClickAdvanced
                    )
                }
                item("about") {
                    ExpressiveCardPreference(
                        title = stringResource(R.string.settings_screen_about),
                        subtitle = "App info, links, and developers",
                        icon = R.drawable.ic_settings_about,
                        iconBgColor = Color(0xFFE53935), // Red
                        onClick = onClickAbout
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpressiveCardPreference(
    title: String,
    subtitle: String?,
    icon: Int,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    initPreview(LocalContext.current)
    Theme(previewDark) {
        Surface {
            MainSettingsScreen({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})
        }
    }
}
