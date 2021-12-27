package com.mhmd.newsapp.business.data.util

interface ModelMapper <T, Model> {

    fun mapToModel(model: T): Model

    fun mapFromModel(model: Model): T
}
