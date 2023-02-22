package com.moyee.android.domain

import com.google.android.gms.maps.model.LatLng

/**
 * 내 위치 기반 근처 유저의 정보
 * TODO: 서버단과 상의 후 데이터 클래스 변경 예정
 * */
data class UserLocation(
    val id: Int? = null,
    val location: LatLng,
    val imageUrl: String
)