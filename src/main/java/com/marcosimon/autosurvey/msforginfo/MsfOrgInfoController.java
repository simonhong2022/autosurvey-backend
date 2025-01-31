package com.marcosimon.autosurvey.msforginfo;

import com.marcosimon.autosurvey.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orginfos")
@CrossOrigin(origins = {"https://autosurvey.vercel.app", "http://localhost:3000"})
public class MsfOrgInfoController {

  private final MsfOrgInfoService msfOrgInfoService;

  @GetMapping("one/final")
  public ResponseEntity<FinalOrgInfoDTO> getFinalOrgInfo( @RequestBody @NotNull NewFinalOrgInfoDTO newFunctionInfoDTO) {
    return ResponseEntity.ok(msfOrgInfoService.getFinalOrgInfo(newFunctionInfoDTO));
  }

  @GetMapping("final")
  public ResponseEntity<List<FinalOrgInfoDTO>> getAllFinalOrgInfo() {
    return ResponseEntity.ok(msfOrgInfoService.getAllFinalOrgInfo());
  }

  @GetMapping
  public ResponseEntity<List<MsfOrgInfo>> getAllMsfOrgInfo() {
    return ResponseEntity.ok(msfOrgInfoService.getAllMsfOrgInfo());
  }

  @GetMapping("{id}")
  public ResponseEntity<MsfOrgInfo> getMsfOrgInfo(@PathVariable @NotEmpty Long id) {
    return ResponseEntity.ok(msfOrgInfoService.getMsfOrgInfoById(id));
  }

  @PostMapping
  public ResponseEntity<MsfOrgInfo> createMsfOrgInfo( @RequestBody @NotNull NewMsfOrgInfoDTO newMsfOrgInfoDTO, HttpServletRequest req) {
    MsfOrgInfo msfOrgInfo = msfOrgInfoService.addMsfOrgInfo(newMsfOrgInfoDTO);
    URI location = URI.create((req.getRequestURI() + "/" + msfOrgInfo.getOrgId()));
    return ResponseEntity.created(location).body(msfOrgInfo);

  }

  @PatchMapping("{id}")
  public ResponseEntity<MsfOrgInfo> updateMsfOrgInfo(@PathVariable @NotEmpty Long id, @RequestBody @NotNull NewMsfOrgInfoDTO newMsfOrgInfoDTO) {
    return ResponseEntity.ok(msfOrgInfoService.updateMsfOrgInfo(id, newMsfOrgInfoDTO));
  }

  @DeleteMapping("{id}")
  public void deleteMsfOrgInfo(@PathVariable @NotEmpty Long id) {
    msfOrgInfoService.deleteMsfOrgInfo(id);
  }

}
