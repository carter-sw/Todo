package com.example.javaerp.Todo.web.error;

public interface GlobalExceptionMessage {
    String TodoNotFoundExceptionMessage = "해당하는 Todo를 찾을 수 없습니다: ";
    String EntityNotFoundExceptionMessage = "해당하는 회원을 찾을 수 없습니다.";
    String Fileupload_errorMessage = "파일 업로드 중 오류가 발생했습니다.";
    String Failed_create_memo_Message = "메모를 생성하는데 오류가 발생했습니다.";
    String Failed_memos_todo_Message = "todo에 해당하는 메모를 찾을 수 없습니다.";

    String Todo_not_found = "Todo를 찾을 수 없습니다.";

    String Schedule_not_found = "스케줄을 찾을 수 없습니다.";

    String Failed_send_notification = "알림을 보내지 못했습니다.";

    String Failed_retrieve_notifications = "최근 알림을 찾지 못했습니다.";
}
