package com.cursosant.forecastweatherbase.mainModule.viewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.AssumptionViolatedException
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(val dispatcher : TestCoroutineDispatcher = TestCoroutineDispatcher())
    : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun apply(base: Statement, description: Description): Statement {
        return super.apply(base, description)
    }

    override fun succeeded(description: Description?) {
        super.succeeded(description)
    }

    override fun failed(e: Throwable?, description: Description?) {
        super.failed(e, description)
    }

    override fun skipped(e: AssumptionViolatedException?, description: Description?) {
        super.skipped(e, description)
    }

    override fun skipped(
        e: org.junit.internal.AssumptionViolatedException?,
        description: Description?
    ) {
        super.skipped(e, description)
    }

    override fun finished(description: Description) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}