package com.example.sembakoapp.data;

import com.example.sembakoapp.BuildConfig;
import com.example.sembakoapp.data.remote.ApiClient;
import com.example.sembakoapp.data.remote.RemoteSembakoRepository;

/** Single place to swap the data source. */
public final class ServiceLocator {

  private static SembakoRepository repository;

  private ServiceLocator() {}

  public static synchronized SembakoRepository repository() {
    if (repository == null) {
      repository =
          BuildConfig.USE_REMOTE_API
              ? new RemoteSembakoRepository(ApiClient.create())
              : new FakeSembakoRepository();
    }
    return repository;
  }

  public static synchronized void setRepository(SembakoRepository repo) {
    repository = repo;
  }
}
