/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara.exception;

/**
 * Indicates that the configuration is wrong or some error attributable to the user happened. For
 * example wrong flag usage, errors in fields or errors that we discover during execution.
 */
public class ValidationException extends Exception {

  private final boolean retryable;
  
  public ValidationException(String message) {
    this(false, message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
    retryable = cause instanceof ValidationException
        && ((ValidationException) cause).retryable;
  }

  private ValidationException(boolean retryable, String message) {
    super(message);
    this.retryable = retryable;
  }

  /**
   * Check a condition and throw {@link ValidationException} if false
   * @throws ValidationException if {@code condition} is false
   */
  public static void checkCondition(boolean condition, String format, Object... args)
      throws ValidationException {
    if (!condition) {
      throw new ValidationException(String.format(format, args));
    }
  }

  /** Throw a {@link ValidationException} that can be retried */
  public static ValidationException retriableException(String message) {
    return new ValidationException(true, message);
  }

  /** If the execution could be retried and succeed */
  public boolean isRetryable() {
    return retryable;
  }
}
