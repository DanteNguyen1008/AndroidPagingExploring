package com.cat.reddithexagonal.modules

import com.cat.domain.interfaces.framework.IFrameworkContants
import com.cat.domain.interfaces.interactor.IPostInteractor
import com.cat.domain.interfaces.repository.IDataSource
import com.cat.domain.usecase.PostInteractor
import com.cat.framework.DataSource
import com.cat.framework.local.DiskDataSource
import com.cat.framework.local.database.RedditDatabase
import com.cat.framework.network.RedditNetworkDataSource
import com.cat.presentation.viewmodel.ListViewModel
import com.cat.reddithexagonal.framework.FrameworkConstants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { ListViewModel() }
}

val interactorModules = module {
    factory<IPostInteractor> { PostInteractor() }
}

val frameworkModules = module {
    single<IFrameworkContants> { FrameworkConstants() }
    single<IDataSource> { DataSource() }
    single<IDataSource.IDiskDataSource> { DiskDataSource() }
    single<IDataSource.INetworkDataSource> { RedditNetworkDataSource() }
    single { RedditDatabase.create(get()) }
}