package com.admin_bot.environment

import com.admin_bot.features.login.repository.LoginRepository
import com.admin_bot.features.registration.model.RegistrationRepository
import com.admin_bot.helpers.Validators
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.repository.MockLoginRepository
import com.admin_bot.plugins.mocks.repository.MockRegistrationRepository


class MockEnvironment(mockDatabase: MockDatabase) : AppEnvironment {
    override val passwordValidator = Validators.Companion::validatePassword
    override val registrationRepository: RegistrationRepository
    override val loginRepository: LoginRepository

    init {
        registrationRepository = MockRegistrationRepository(mockDatabase)
        loginRepository = MockLoginRepository(mockDatabase)
    }
}