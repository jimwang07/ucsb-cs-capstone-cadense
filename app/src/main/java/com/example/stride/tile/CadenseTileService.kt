package com.example.stride.tile

import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.example.stride.presentation.MainActivity
import com.google.common.util.concurrent.Futures

// ProtoLayout imports
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.TimelineBuilders

// Color Palette (matching AdjustMetronomeScreen)
private val EmeraldGreen = ColorBuilders.argb(0xFF34D399.toInt())
private val Black = ColorBuilders.argb(0xFF000000.toInt())
private val White = ColorBuilders.argb(0xFFFFFFFF.toInt())

class CadenseTileService : TileService() {

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ) = Futures.immediateFuture(

        TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(

                                        // ---- ROOT CONTAINER ---- //
                                        LayoutElementBuilders.Box.Builder()
                                            .setWidth(DimensionBuilders.expand())
                                            .setHeight(DimensionBuilders.expand())
                                            .setModifiers(
                                                ModifiersBuilders.Modifiers.Builder()
                                                    .setClickable(
                                                        ModifiersBuilders.Clickable.Builder()
                                                            .setOnClick(
                                                                ActionBuilders.LaunchAction.Builder()
                                                                    .setAndroidActivity(
                                                                        ActionBuilders.AndroidActivity.Builder()
                                                                            .setClassName(
                                                                                MainActivity::class.java.name
                                                                            )
                                                                            .setPackageName(this.packageName)
                                                                            .build()
                                                                    )
                                                                    .build()
                                                            )
                                                            .build()
                                                    )
                                                    .setBackground(
                                                        ModifiersBuilders.Background.Builder()
                                                            .setColor(Black)
                                                            .build()
                                                    )
                                                    .build()
                                            )
                                            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
                                            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)

                                            // ---- CENTERED COLUMN ---- //
                                            .addContent(
                                                LayoutElementBuilders.Column.Builder()
                                                    .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)

                                                    // Main Brand Name
                                                    .addContent(
                                                        LayoutElementBuilders.Text.Builder()
                                                            .setText("CADENSE")
                                                            .setFontStyle(
                                                                LayoutElementBuilders.FontStyle.Builder()
                                                                    .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                                                                    .setSize(
                                                                        DimensionBuilders.sp(
                                                                            40f
                                                                        )
                                                                    )
                                                                    .setColor(EmeraldGreen)
                                                                    .build()
                                                            )
                                                            .build()
                                                    )

                                                    // Spacer
                                                    .addContent(
                                                        LayoutElementBuilders.Spacer.Builder()
                                                            .setHeight(DimensionBuilders.dp(4f))
                                                            .build()
                                                    )

                                                    // Subtitle / Call to Action
                                                    .addContent(
                                                        LayoutElementBuilders.Text.Builder()
                                                            .setText("Tap to Open")
                                                            .setFontStyle(
                                                                LayoutElementBuilders.FontStyle.Builder()
                                                                    .setSize(
                                                                        DimensionBuilders.sp(
                                                                            32f
                                                                        )
                                                                    )
                                                                    .setColor(White)
                                                                    .build()
                                                            )
                                                            .build()
                                                    )
                                                    .build()
                                            )
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    )

    override fun onResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ) = Futures.immediateFuture(
        ResourceBuilders.Resources.Builder()
            .setVersion("1")
            .build()
    )
}