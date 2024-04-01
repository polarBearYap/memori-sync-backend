package com.memori.memori_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memori.memori_api.requests.PullRequest;
import com.memori.memori_api.requests.PullSpecificRequest;
import com.memori.memori_api.requests.UploadRequest;
import com.memori.memori_api.responses.PullResponse;
import com.memori.memori_api.responses.UploadResponse;
import com.memori.memori_service.dtos.PaginatedDto;
import com.memori.memori_service.dtos.PullSyncResponseDto;
import com.memori.memori_service.dtos.SyncEntityDto;
import com.memori.memori_service.services.GeneralSyncService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/schedulecard", produces = "application/json")
public class SyncController {

    private final GeneralSyncService generalSyncService;

    public SyncController(GeneralSyncService generalSyncService) {
        this.generalSyncService = generalSyncService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UploadResponse> handleCustomException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UploadResponse
                        .builder()
                        .message("An error occurred: " + ex.getMessage())
                        .build());
    }

    private PullResponse buildPullReponse(PaginatedDto<SyncEntityDto> res) {
        return PullResponse
                .builder()
                .message("The created records are fetched successfully.")
                .totalPages(res.getTotalPages())
                .totalElements(res.getTotalElements())
                .currentPageNumber(res.getCurrentPageNumber())
                .hasNextPage(res.getHasNextPage())
                .hasPreviousPage(res.getHasPreviousPage())
                .items(res.getContent())
                .build();
    }

    @GetMapping("pullcreate")
    public ResponseEntity<PullResponse> pullCreate(@Valid @ModelAttribute PullRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(PullResponse
                    .builder()
                    .message("Validation errors: " + bindingResult.getAllErrors())
                    .build());
        }
        PaginatedDto<SyncEntityDto> res = generalSyncService.pullCreation(request.getLastSyncDateTimeStr(),
                request.getUserId(),
                request.getPageNumber(), request.getPageSize());

        return ResponseEntity.ok().body(buildPullReponse(res));
    }

    @GetMapping("pullupdate")
    public ResponseEntity<PullResponse> pullUpdate(@ModelAttribute PullRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(PullResponse
                    .builder()
                    .message("Validation errors: " + bindingResult.getAllErrors())
                    .build());
        }
        PaginatedDto<SyncEntityDto> res = generalSyncService.pullUpdate(request.getLastSyncDateTimeStr(),
                request.getUserId(),
                request.getPageNumber(), request.getPageSize());

        return ResponseEntity.ok().body(buildPullReponse(res));
    }

    @GetMapping("pulldelete")
    public ResponseEntity<PullResponse> pullDelete(@ModelAttribute PullRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(PullResponse
                    .builder()
                    .message("Validation errors: " + bindingResult.getAllErrors())
                    .build());
        }
        PaginatedDto<SyncEntityDto> res = generalSyncService.pullDeletion(request.getLastSyncDateTimeStr(),
                request.getUserId(),
                request.getPageNumber(), request.getPageSize());

        return ResponseEntity.ok().body(buildPullReponse(res));
    }

    @GetMapping("pullspecific")
    public ResponseEntity<PullResponse> pullSpecific(@ModelAttribute PullSpecificRequest request,
    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(PullResponse
                    .builder()
                    .message("Validation errors: " + bindingResult.getAllErrors())
                    .build());
        }
        PaginatedDto<SyncEntityDto> res = generalSyncService.pullSpecific(request.getEntityIds(), request.getUserId());

        return ResponseEntity.ok().body(buildPullReponse(res));
    }

    @PostMapping(path = "upload")
    public ResponseEntity<UploadResponse> normalUpload(@Valid @RequestBody UploadRequest request,
            BindingResult result) {
        if (result.hasErrors()) {
            UploadResponse resp = UploadResponse.builder().message("Invalid request: " + result.getAllErrors()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        PullSyncResponseDto res = generalSyncService.normalPush(request.items);

        return ResponseEntity.ok().body(UploadResponse
                .builder()
                .message("The sync is pushed successfully. Below are the conflicted rows.")
                .conflictedItems(res.getConflictedItems())
                .successfulItems(res.getSuccessfulItems())
                .build());
    }

    @PostMapping(path = "forceupload")
    public ResponseEntity<UploadResponse> overrideUpload(@Valid @RequestBody UploadRequest request,
            BindingResult result) {
        if (result.hasErrors()) {
            UploadResponse resp = UploadResponse.builder().message("Invalid request: " + result.getAllErrors()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        PullSyncResponseDto res = generalSyncService.overridePush(request.items);

        return ResponseEntity.ok().body(UploadResponse
                .builder()
                .message("The sync is overwritten successfully. Below are the problematic rows.")
                .conflictedItems(res.getConflictedItems())
                .successfulItems(res.getSuccessfulItems())
                .build());
    }
}
