package com.otus.galaxy13.web.server.application.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Processor implements RequestProcessor {
    final Logger logger = LoggerFactory.getLogger(Processor.class);
}
