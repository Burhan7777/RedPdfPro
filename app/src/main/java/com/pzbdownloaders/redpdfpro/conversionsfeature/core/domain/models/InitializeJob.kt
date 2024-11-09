package com.pzbdownloaders.redpdfpro.conversionsfeature.core.domain.models

import com.google.gson.annotations.SerializedName


data class InitializeJob(
    val id: Long,
    val key: String,
    val status: String,
    val sandbox: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("finished_at")
    val finishedAt: Any?,
    @SerializedName("source_file")
    val sourceFile: SourceFile,
    @SerializedName("target_files")
    val targetFiles: List<Any?>,
    @SerializedName("target_format")
    val targetFormat: String,
    @SerializedName("credit_cost")
    val creditCost: Long,
)

data class SourceFile(
    val id: Long,
    val name: String,
    val size: Long,
)