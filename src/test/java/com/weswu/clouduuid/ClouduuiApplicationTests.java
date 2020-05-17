package com.weswu.clouduuid;

import com.weswu.clouduuid.service.SequentialIdService;
import com.weswu.clouduuid.service.CachedSnowFlakeIdService;
import com.weswu.clouduuid.service.SnowFlakeIdService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClouduuiApplicationTests {
	static {
		ClouduuidApplication.setLogbackVariables();
		ClouduuidApplication.setupStackDriver();
	}

	@Test
	void contextLoads() {
		System.out.println("Hello");
	}

	@Test
	void testSequentialId() throws Exception {
		SequentialIdService sequentialIdService = SequentialIdService.getInstance();
		long uuid = sequentialIdService.getUuid("order-prod");
		assert(uuid != 0l);
	}
	@Test
	void testSnowFlakelId() throws Exception {
		SnowFlakeIdService snowFlakeIdService = SnowFlakeIdService.getInstance();
		long uuid = snowFlakeIdService.getUuid("order-prod");
		assert(uuid != 0l);
	}

	@Test
	void testCachedSnowFlakelId() throws Exception {
		CachedSnowFlakeIdService cachedSnowFlakeIdService = CachedSnowFlakeIdService.getInstance();
		long uuid = cachedSnowFlakeIdService.getUuid("order-prod");
		assert(uuid != 0l);
	}


}
