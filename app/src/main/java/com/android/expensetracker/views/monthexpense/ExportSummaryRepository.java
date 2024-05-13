package com.android.expensetracker.views.monthexpense;

import com.android.expensetracker.db.AppDatabase;

public class ExportSummaryRepository {

    private final ExportSummaryDao exportSummaryDao;

    public ExportSummaryRepository(){
        this.exportSummaryDao = AppDatabase.getInstance().getExportSummaryDao();
    }

    public void exportSummary(ExportSummaryEntity entity){
        exportSummaryDao.add(entity);
    }

    public ExportSummaryEntity getSummary(int month , int year){
        return exportSummaryDao.get(month,year);
    }

    public double getTotalExportedExpenseOfYear(int year){
        return exportSummaryDao.getTotalExportedExpenseOfYear(year);
    }

    public double getTotalExportedExpenseOfMonthYear(int month , int year){
        return exportSummaryDao.getTotalExportedExpenseOfMonthYear(month , year);
    }
}
