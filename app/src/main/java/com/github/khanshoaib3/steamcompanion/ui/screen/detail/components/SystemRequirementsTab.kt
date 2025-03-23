package com.github.khanshoaib3.steamcompanion.ui.screen.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.data.model.detail.Requirements
import com.github.khanshoaib3.steamcompanion.ui.screen.detail.GameData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Composable
fun SystemRequirementsTab(modifier: Modifier = Modifier, gameData: GameData) {
    Column(modifier = modifier.padding(dimensionResource(R.dimen.padding_small))) {
        val json = Json { ignoreUnknownKeys = true }
        if (gameData.content?.data?.platforms?.windows == true && gameData.content.data.pcRequirements is JsonObject) {
            Text(
                "Windows Requirements",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            val pcRequirements =
                json.decodeFromJsonElement<Requirements>(gameData.content.data.pcRequirements)

            if (pcRequirements.minimum != null) {
                Text(
                    AnnotatedString.fromHtml(
                        pcRequirements.minimum.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
            if (pcRequirements.recommended != null) {
                Text(
                    AnnotatedString.fromHtml(
                        pcRequirements.recommended.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
        }
        if (gameData.content?.data?.platforms?.linux == true && gameData.content.data.linuxRequirements is JsonObject) {
            Text(
                "Linux Requirements",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            val linuxRequirements =
                json.decodeFromJsonElement<Requirements>(gameData.content.data.linuxRequirements)
            if (linuxRequirements.minimum != null) {
                Text(
                    AnnotatedString.fromHtml(
                        linuxRequirements.minimum.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
            if (linuxRequirements.recommended != null) {
                Text(
                    AnnotatedString.fromHtml(
                        linuxRequirements.recommended.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
        }
        if (gameData.content?.data?.platforms?.mac == true && gameData.content.data.macRequirements is JsonObject) {
            Text(
                "MacOS Requirements",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            val macRequirements =
                json.decodeFromJsonElement<Requirements>(gameData.content.data.macRequirements)
            if (macRequirements.minimum != null) {
                Text(
                    AnnotatedString.fromHtml(
                        macRequirements.minimum.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
            if (macRequirements.recommended != null) {
                Text(
                    AnnotatedString.fromHtml(
                        macRequirements.recommended.replace("<br>", "").replace("<br/>", "")
                    )
                )
            }
        }
    }
}