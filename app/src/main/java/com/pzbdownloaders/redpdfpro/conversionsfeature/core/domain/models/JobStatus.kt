package com.pzbdownloaders.redpdfpro.conversionsfeature.core.domain.models

data class JobStatus(
    val id: Long,
    val key: String,
    val status: String,
    val sandbox: Boolean,
    val created_at: String,
    val finished_at: String,
    val source_file: SourceFileJobStatus,
    val target_files: List<TargetFile>,
    val target_format: String,
    val credit_cost: Long,
)

data class SourceFileJobStatus(
    val id: Long,
    val name: String,
    val size: Long,
)

data class TargetFile(
    val id: Long,
    val name: String,
    val size: Long,
)