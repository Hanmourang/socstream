/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */
package com.acmutv.socstream.common.meta;

import com.acmutv.socstream.common.meta.serial.MatchMetadataYamlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The collection of metadata about the match.
 *
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class MatchService {

  /**
   * The logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MatchService.class);

  /**
   * Parses metadata about the match from the given file.
   * @param path the filename.
   * @return the metadata about the match.
   * @throws IOException when metadata cannot be read.
   */
  public static Match fromYamlFile(Path path) throws IOException {
    final YAMLMapper mapper = new MatchMetadataYamlMapper();
    Match metadata;
    try (Reader rd = Files.newBufferedReader(path, Charset.defaultCharset())) {
      metadata = mapper.readValue(rd, Match.class);
    }
    return metadata;
  }

  /**
   * Parses metadata about the match from the given file.
   * @param in the file.
   * @return the metadata about the match.
   * @throws IOException when metadata cannot be read.
   */
  public static Match fromYamlFile(InputStream in) throws IOException {
    final YAMLMapper mapper = new MatchMetadataYamlMapper();
    Match metadata;
    metadata = mapper.readValue(in, Match.class);
    return metadata;
  }

  /**
   * Collects ids of sensors to be ignored.
   * @param match the metdata about the match.
   * @return the collection of sensors id to be ignored.
   */
  public static Set<Long> collectIgnoredSensors(Match match) {
    Set<Long> ignoredSensors = new HashSet<>();

    ignoredSensors.addAll(match.getBallsHalf1());
    ignoredSensors.addAll(match.getBallsHalf2());

    ignoredSensors.add(match.getReferee().getLegLeft());
    ignoredSensors.add(match.getReferee().getLegRight());

    ignoredSensors.add(match.getTeamA().getPlayers().get(0).getArmLeft());
    ignoredSensors.add(match.getTeamA().getPlayers().get(0).getArmRight());
    ignoredSensors.add(match.getTeamB().getPlayers().get(0).getArmLeft());
    ignoredSensors.add(match.getTeamB().getPlayers().get(0).getArmRight());

    return ignoredSensors;
  }

  /**
   * Collects ids of sensors to be ignored.
   * @param match the metdata about the match.
   * @return the collection of sensors id to be ignored.
   */
  public static Map<Long,Long> collectSid2Pid(Match match) {
    Map<Long,Long> sid2Pid = new HashMap<>();

    long pidA = 100;
    for (Person player : match.getTeamA().getPlayers()) {
      for (Long sensor : player.getAllSensors()) {
        if (sensor == null) continue;
        sid2Pid.put(sensor, pidA);
      }
      pidA++;
    }

    long pidB = 200;
    for (Person player : match.getTeamB().getPlayers()) {
      for (Long sensor : player.getAllSensors()) {
        if (sensor == null) continue;
        sid2Pid.put(sensor, pidB);
      }
      pidB++;
    }

    return sid2Pid;
  }
}
