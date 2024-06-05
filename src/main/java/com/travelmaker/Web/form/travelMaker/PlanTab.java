package com.travelmaker.Web.form.travelMaker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlanTab {

  @JsonProperty("planItems")
  private List<PlanItem> planItems;
}
