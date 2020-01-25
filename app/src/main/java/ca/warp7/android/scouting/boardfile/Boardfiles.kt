@file:Suppress("unused")

package ca.warp7.android.scouting.boardfile

import java.io.File

fun File.toBoardfile(): Boardfile {
    TODO()
}

val exampleBoardfile = Boardfile(
    eventName = "ONT Science Division",
    eventKey = "oncmp1",
    matchSchedule = MatchSchedule(exampleMatchSchedule),
    robotScoutTemplate = ScoutTemplate(
        listOf(
            TemplateScreen(
                "Auto", listOf(
                    listOf(
                        TemplateField("trench_intake", FieldType.Button),
                        TemplateField("fed", FieldType.Button),
                        TemplateField("other_intake", FieldType.Button)
                    ),
                    listOf(
                        TemplateField("low", FieldType.Button),
                        TemplateField("inner", FieldType.Button),
                        TemplateField("outer", FieldType.Button)
                    ),
                    listOf(
                        TemplateField("low_miss", FieldType.Button),
                        TemplateField("high_miss", FieldType.Button)
                    ),
                    listOf(
                        TemplateField(
                            "field_area", FieldType.Toggle,
                            listOf("Cross", "Mid", "Init", "Target")
                        )
                    )
                )
            ),
            TemplateScreen(
                "Teleop", listOf(
                    listOf(
                        TemplateField("control_panel", FieldType.Switch),
                        TemplateField("defending", FieldType.Switch),
                        TemplateField("resisting", FieldType.Switch)
                    ),
                    listOf(
                        TemplateField("low", FieldType.Button),
                        TemplateField("inner", FieldType.Button),
                        TemplateField("outer", FieldType.Button)
                    ),
                    listOf(
                        TemplateField("low_miss", FieldType.Button),
                        TemplateField("high_miss", FieldType.Button)
                    ),
                    listOf(
                        TemplateField(
                            "field_area", FieldType.Toggle,
                            listOf("Cross", "Mid", "Init", "Target")
                        )
                    )
                )
            ),
            TemplateScreen(
                "Endgame", listOf(
                    listOf(
                        TemplateField("climb", FieldType.Toggle,
                            listOf("N/A", "Attempt", "Success"))
                    ),
                    listOf(
                        TemplateField(
                            "climb_location", FieldType.Toggle,
                            listOf("Middle", "Up", "Down", "Balanced")
                        )
                    ),
                    listOf(
                        TemplateField("balanced_after_climb", FieldType.Checkbox)
                    ),
                    listOf(
                        TemplateField("active_movement", FieldType.Checkbox)
                    )
                )
            )
        ), listOf()
    ),
    superScoutTemplate = ScoutTemplate(listOf(), listOf())
)