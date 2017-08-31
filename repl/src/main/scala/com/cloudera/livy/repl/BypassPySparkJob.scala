/*
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.livy.repl

import java.nio.charset.StandardCharsets

import com.cloudera.livy.{Job, JobContext}
import com.cloudera.livy.sessions._

class BypassPySparkJob(
    serializedJob: Array[Byte],
    pi: PythonInterpreter) extends Job[Array[Byte]] {

  override def call(jc: JobContext): Array[Byte] = {
    val resultByteArray = pi.pysparkJobProcessor.processBypassJob(serializedJob)
    val resultString = new String(resultByteArray, StandardCharsets.UTF_8)
    if (resultString.startsWith("Client job error:")) {
      throw new PythonJobException(resultString)
    }
    resultByteArray
  }
}
