package com.podcastify.implementor;

import com.podcastify.service.LogService;
import com.podcastify.repository.LogRepository;
import com.podcastify.model.LogModel;
import com.podcastify.constant.Response;

import java.sql.SQLException;

public class LogServiceImpl implements LogService {
    private LogRepository lr = new LogRepository();

    public int addLog(LogModel lm) {
        try {
            lr.addLog(lm);
            return Response.HTTP_STATUS_ACCEPTED;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.HTTP_STATUS_INTERNAL_SERVER_ERROR;
    }
}
